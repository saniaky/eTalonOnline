package com.etalon;

import com.etalon.model.CodexBook;
import com.etalon.model.CodexNode;
import com.etalon.model.CodexNodeType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.etalon.html.CacheManager.getCodexHTML;
import static com.etalon.html.CacheManager.saveCodexJSON;
import static com.etalon.html.CodexNodeBuilder.buildNode;
import static com.etalon.html.ParsingUtils.*;
import static com.etalon.model.CodexNodeType.*;
import static java.util.Map.entry;

@Slf4j
public class App {

    private static final Map<String, String> map = Map.ofEntries(
            entry("hk0000441", "Банковский кодекс Республики Беларусь"),
            entry("hk0800412", "Бюджетный кодекс Республики Беларусь"),
            entry("hk1400149", "Водный кодекс Республики Беларусь"),
            entry("hk0600117", "Воздушный кодекс Республики Беларусь"),
            entry("hk9800218", "Гражданский кодекс Республики Беларусь"),
            entry("hk9900238", "Гражданский процессуальный кодекс Республики Беларусь"),
            entry("hk1200428", "Жилищный кодекс Республики Беларусь"),
            entry("hk0000370", "Избирательный кодекс Республики Беларусь"),
            entry("hk9900278", "Кодекс Республики Беларусь о браке и семье"),
            entry("hk0800425", "Кодекс Республики Беларусь о земле"),
            entry("hk0800406", "Кодекс Республики Беларусь о недрах"),
            entry("hk0600139", "Кодекс Республики Беларусь о судоустройстве и статусе судей"),
            entry("hk0300194", "Кодекс Республики Беларусь об административных правонарушениях"),
            entry("hk1100243", "Кодекс Республики Беларусь об образовании"),
            entry("hk0200118", "Кодекс внутреннего водного транспорта Республики Беларусь"),
            entry("hk9900321", "Кодекс торгового мореплавания Республики Беларусь"),
            entry("hk1600413", "Кодэкс Рэспублiкi Беларусь аб культуры"),
            entry("hk1500332", "Лесной кодекс Республики Беларусь"),
            entry("hk0200166", "Налоговый кодекс Республики Беларусь (Общая часть)"),
            entry("hk0900071", "Налоговый кодекс Республики Беларусь (Особенная часть)"),
            entry("hk0600194", "Процессуально-исполнительный кодекс Республики Беларусь об административных правонарушениях"),
            entry("hk9900296", "Трудовой кодекс Республики Беларусь"),
            entry("hk0000365", "Уголовно-исполнительный кодекс Республики Беларусь"),
            entry("hk9900295", "Уголовно-процессуальный кодекс Республики Беларусь"),
            entry("hk9900275", "Уголовный кодекс Республики Беларусь"),
            entry("hk9800219", "Хозяйственный процессуальный кодекс Республики Беларусь")
    );

    public static void main(String[] args) throws IOException {
        var main = new App();
        for (var entry : map.entrySet()) {
            var codex = main.createCodex(entry.getKey(), entry.getValue());
            saveCodexJSON(codex);
        }
    }

    public CodexBook createCodex(String codexId, String codexName) throws IOException {
        var doc = getCodexHTML(codexId);
        var entries = doc.selectFirst(".Section1").children();

        // Pre-processing steps
        printStatistics(codexId, codexName, entries);

        // Process html entries and build codex
        var changes = buildTree(CHANGE_ENTRY, entries, 0);
        var codexParts = buildTree(CODEX_PART, entries, changes.getNewOffset());
        if (codexParts.getList().isEmpty()) {
            // В кодексе нет частей, пробуем разделы
            codexParts = buildTree(SECTION, entries, changes.getNewOffset());
        }
        if (codexParts.getList().isEmpty()) {
            // В кодексе нет разделов, пробуем главы
            codexParts = buildTree(CHAPTER, entries, changes.getNewOffset());
        }
        return CodexBook.builder()
                .id(codexId)
                .date(getUniqueValue(entries, CODEX_DATE))
                .number(getUniqueValue(entries, CODEX_NUMBER))
                .acceptedDetails(getUniqueValue(entries, CODEX_ACCEPTED))
                .name(codexName)
                .codexChanges(changes.getList())
                .children(codexParts.getList())
                .build();
    }

    private void printStatistics(String codexId, String codexName, Elements entries) {
        log.info("Parsing '{} - {}'", codexId, codexName);
        var types = new HashSet<String>();
        var unknown = new HashSet<String>();
        int emptyCount = 0;
        for (Element entry : entries) {
            types.add(entry.className());
            if (!isKnown(entry.className())) unknown.add(entry.className());
            emptyCount += isEmpty(entry) ? 1 : 0;
        }
        log.info("  types: {}, empty: {}, unknown: {}", types.size(), emptyCount, unknown.size());
        log.info("  all types: {}", String.join(" | ", types));
        if (!unknown.isEmpty()) {
            log.info("  found unknown types: {}", String.join(" | ", unknown));
        }
    }

    private NodeResult buildTree(CodexNodeType nodeType, Elements elements, int offset) {
        var list = new ArrayList<CodexNode>();
        int newOffset = offset;

        for (int idx = offset; idx < elements.size(); idx++) {
            var element = elements.get(idx);
            if (isGoingUp(element, nodeType)) {
                newOffset = idx - 1;
                break;
            }
            if (isFit(element, nodeType)) {
                var node = buildNode(nodeType, element);
                var nodeChildren = new ArrayList<CodexNode>(0);
                for (CodexNodeType childType : nodeType.getFirstDeepChildren()) {
                    var children = buildTree(childType, elements, idx + 1);
                    nodeChildren.addAll(children.getList());
                    idx = children.getNewOffset();
                }
                node.setChildren(nodeChildren);
                list.add(node);
            }
        }

        return new NodeResult(list, newOffset);
    }

    @Data
    @RequiredArgsConstructor
    private static class NodeResult {
        private final List<CodexNode> list;
        private final Integer newOffset;
    }

}
