package com.company;

import com.company.model.*;
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
import static com.company.SizeTest.validateUPK;

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
        validateUPK(upk);
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
                String numberId = element.attr("id");
                String name = stripHTML(element.html().split("<br>")[1]);
                var pair = buildChapters(elements, i + 1);
                i = pair.getKey();
                Section section = Section.builder()
                        .numberId(numberId)
                        .number("Раздел " + numberId)
                        .name(name)
                        .chapters(pair.getValue())
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
                String numberId = stripHTML(element.attr("id"));
                String name = stripHTML(element.html().split("<br>")[1]);
                var pair = buildArticles(elements, i + 1);
                i = pair.getKey();
                Chapter chapter = Chapter.builder()
                        .numberId(numberId)
                        .number("ГЛАВА " + numberId)
                        .name(name)
                        .articles(pair.getValue())
                        .build();
                chapters.add(chapter);
            } else if (isCodexPart(element) || isSection(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            } else {
                log.info("Chapter - trash element?");
            }
        }
        return new Pair<>(newOffset, chapters);
    }

    private static Pair<Integer, List<Article>> buildArticles(Elements elements, int offset) {
        int newOffset = offset;
        var articles = new ArrayList<Article>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticle(element)) {
                String number = stripHTML(element.attr("id"));
                String name = element.text().replaceAll("Статья [0-9]+.", "").trim();
                Article article = Article.builder()
                        .numberId(number)
                        .number("Статья " + number)
                        .name(name)
                        .text("")
                        .articleParts(Collections.emptyList())
                        .articleParagraphs(Collections.emptyList())
                        .build();
                articles.add(article);
            } else if (isCodexPart(element) || isSection(element) || isChapter(element)) {
                newOffset = i - 1;
                break;
            }
        }

        return new Pair<>(newOffset, articles);
    }

}
