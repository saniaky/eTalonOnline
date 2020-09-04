package com.company;

import com.company.model.Chapter;
import com.company.model.CodexPart;
import com.company.model.Section;
import com.company.model.UPK;
import javafx.util.Pair;
import lombok.SneakyThrows;
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
import static com.company.SizeTest.validateUPKSize;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        new Main().start();
    }

    @SneakyThrows
    public void start() {
        // Document doc = Jsoup.connect("https://etalonline.by/document/?regnum=HK9900295&q_id=").get();
        Document doc = Jsoup.parse(getClass().getClassLoader().getResourceAsStream("kodeks.html"), "UTF-8", "");
        Elements elements = doc.selectFirst(".Section1").children();
        // 710 - 4410
        UPK upk = buildUPK(elements);
        validateUPKSize(upk);
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
                        .numberId(element.attr("id"))
                        .number(stripHTML(title[0]))
                        .name(stripHTML(title[1]))
                        .chapters(chapters)
                        .build();
                sections.add(section);
            } else if (isCodexPart(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            } else {
                log.info("Section - trash element {}", element);
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
                String number = stripHTML(element.attr("id"));
                String name = stripHTML(title[1]);
                Chapter chapter = Chapter.builder()
                        .numberId(number)
                        .number("ГЛАВА " + number)
                        .name(name)
                        .articles(Collections.emptyList())
                        .build();
                chapters.add(chapter);
            } else if (isCodexPart(element) || isSection(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            } else {
                // todo - remove later
                if (!isArticle(element)
                        && !isArticlePart(element)
                        && !isArticleParagraph(element)
                        && !isComment(element)
                        && !isNote(element)) {
                    log.info("Chapter - trash element?");
                }
            }
        }
        return new Pair<>(newOffset, chapters);
    }

}
