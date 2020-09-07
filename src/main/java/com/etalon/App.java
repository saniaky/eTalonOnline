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

import static com.etalon.model.CodexNodeType.*;
import static com.etalon.utils.CodexManager.getCodexHTML;
import static com.etalon.utils.CodexManager.saveCodexJSON;
import static com.etalon.utils.CodexNodeBuilder.buildNode;
import static com.etalon.utils.CodexNodeBuilder.getUniqueElement;
import static com.etalon.utils.ParsingUtils.isBlank;
import static com.etalon.utils.ParsingUtils.isKnown;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException {
        var main = new App();
//        main.createCodex("hk0000441", "Банковский кодекс Республики Беларусь");
//        main.createCodex("hk0800412", "Бюджетный кодекс Республики Беларусь");
//        main.createCodex("hk1400149", "Водный кодекс Республики Беларусь");
//        main.createCodex("hk0600117", "Воздушный кодекс Республики Беларусь");
//        main.createCodex("hk9800218", "Гражданский кодекс Республики Беларусь");
//        main.createCodex("hk9900238", "Гражданский процессуальный кодекс Республики Беларусь");
//        main.createCodex("hk1200428", "Жилищный кодекс Республики Беларусь");
//        main.createCodex("hk0000370", "Избирательный кодекс Республики Беларусь");
//        main.createCodex("hk9900278", "Кодекс Республики Беларусь о браке и семье");
//        main.createCodex("hk0800425", "Кодекс Республики Беларусь о земле");
//        main.createCodex("hk0800406", "Кодекс Республики Беларусь о недрах");
//        main.createCodex("hk0600139", "Кодекс Республики Беларусь о судоустройстве и статусе судей");
//        main.createCodex("hk0300194", "Кодекс Республики Беларусь об административных правонарушениях");
//        main.createCodex("hk1100243", "Кодекс Республики Беларусь об образовании");
//        main.createCodex("hk0200118", "Кодекс внутреннего водного транспорта Республики Беларусь");
//        main.createCodex("hk9900321", "Кодекс торгового мореплавания Республики Беларусь");
//        main.createCodex("hk1600413", "Кодэкс Рэспублiкi Беларусь аб культуры");
//        main.createCodex("hk1500332", "Лесной кодекс Республики Беларусь");
//        main.createCodex("hk0200166", "Налоговый кодекс Республики Беларусь (Общая часть)");
//        main.createCodex("hk0900071", "Налоговый кодекс Республики Беларусь (Особенная часть)");
//        main.createCodex("hk0600194", "Процессуально-исполнительный кодекс Республики Беларусь об административных правонарушениях");
//        main.createCodex("hk9900296", "Трудовой кодекс Республики Беларусь");
//        main.createCodex("hk0000365", "Уголовно-исполнительный кодекс Республики Беларусь");
        main.createCodex("hk9900295", "Уголовно-процессуальный кодекс Республики Беларусь");
//        main.createCodex("hk9900275", "Уголовный кодекс Республики Беларусь");
//        main.createCodex("hk9800219", "Хозяйственный процессуальный кодекс Республики Беларусь");
    }

    public CodexBook createCodex(String codexId, String codexName) throws IOException {
        var doc = getCodexHTML(codexId);
        var entries = doc.selectFirst(".Section1").children();

        // Pre-processing steps
        printStatistics(codexId, codexName, entries);

        // Process html entries and build codex
        var changes = buildTree(CHANGE, entries, 0);
        var codexParts = buildTree(CODEX_PART, entries, changes.getNewOffset());
        CodexBook codex = CodexBook.builder()
                .id(codexId)
                .date(getUniqueElement(entries, CODEX_DATE))
                .number(getUniqueElement(entries, CODEX_NUMBER))
                .acceptedDetails(getUniqueElement(entries, CODEX_ACCEPTED))
                .name(codexName)
                .codexChanges(changes.getList())
                .children(codexParts.getList())
                .build();

        // Save results
        log.info(codex.toString());
        saveCodexJSON(codex);
        return codex;
    }

    private void printStatistics(String codexId, String codexName, Elements entries) {
        log.info("Information about the '{} - {}'", codexId, codexName);
        var types = new HashSet<String>();
        var unknown = new HashSet<String>();
        int emptyCount = 0;
        for (Element entry : entries) {
            types.add(entry.className());
            if (!isKnown(entry.className())) unknown.add(entry.className());
            emptyCount += isBlank(entry) ? 1 : 0;
        }
        log.info("types: {}, empty: {}, unknown: {}", types.size(), emptyCount, unknown.size());
        log.info("All types: {}", String.join(" | ", types));
        if (!unknown.isEmpty()) {
            log.info("Found unknown types: {}", String.join(" | ", unknown));
        }
    }

    private NodeResult buildTree(CodexNodeType nodeType, Elements elements, int offset) {
        var list = new ArrayList<CodexNode>();
        int newOffset = offset;

        for (int idx = offset; idx < elements.size(); idx++) {
            var element = elements.get(idx);
            if (nodeType.isGoingUp(element)) {
                newOffset = idx - 1;
                break;
            }
            if (nodeType.isFit(element)) {
                var node = buildNode(nodeType, element);
                for (CodexNodeType childType : nodeType.getFirstDeepChildren()) {
                    var children = buildTree(childType, elements, idx + 1);
                    node.setChildren(children.getList());
                    idx = children.getNewOffset();
                }
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
