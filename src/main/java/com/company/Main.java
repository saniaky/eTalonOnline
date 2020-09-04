package com.company;

import com.company.model.Chapter;
import com.company.model.CodexPart;
import com.company.model.Section;
import com.company.model.UPK;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.company.ParsingUtils.*;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://etalonline.by/document/?regnum=HK9900295&q_id=").get();
        Elements elements = doc.selectFirst(".Section1").children();
        UPK upk = buildUPK(elements);
        // db.save(upk);
        log.info("Done.");
    }

    public static UPK buildUPK(Elements elements) {
        UPK upk = new UPK();
        upk.setCodexParts(buildCodexParts(elements));
        return upk;
    }

    private static List<CodexPart> buildCodexParts(Elements elements) {
        var codexParts = new ArrayList<CodexPart>();
        for (int i = 0; i < elements.size(); i++) {
            var element = elements.get(i);
            if (isCodexPart(element)) {
                String[] title = element.html().split("<br>");
                var pair = buildSections(elements, i + 1);
                var sections = pair.getValue();
                i = pair.getKey(); // skip
                CodexPart codexPart = CodexPart.builder()
                        .number(title[0])
                        .name(title[1])
                        .sections(sections)
                        .build();
                codexParts.add(codexPart);
            }
        }
        return codexParts;
    }

    private static Pair<Integer, List<Section>> buildSections(Elements elements, int offset) {
        int newOffset = offset;
        var sections = new ArrayList<Section>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isSection(element)) {
                String[] title = element.html().split("<br>");
                var pair = buildChapters(elements, i + 1);
                var chapters = pair.getValue();
                i = pair.getKey();
                Section section = Section.builder()
                        .number(stripHTML(title[0]))
                        .name(stripHTML(title[1]))
                        .chapters(chapters)
                        .build();
                sections.add(section);
            } else if (isCodexPart(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            }
        }
        return new Pair<>(newOffset, sections);
    }

    private static Pair<Integer, List<Chapter>> buildChapters(Elements elements, int offset) {
        int newOffset = offset;
        var chapters = new ArrayList<Chapter>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isChapter(element)) {
                String[] title = element.html().split("<br>");
                Chapter chapter = Chapter.builder()
                        .number(stripHTML(title[0]))
                        .name(stripHTML(title[1]))
                        .articles(Collections.emptyList())
                        .build();
                chapters.add(chapter);
            } else if (isCodexPart(element) || isSection(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            }
        }
        return new Pair<>(newOffset, chapters);
    }

}
