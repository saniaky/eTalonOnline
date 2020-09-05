package com.company;

import com.company.model.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.company.ParsingUtils.*;
import static com.company.SizeTest.validateUPK;

@Slf4j
public class Main {

    public static void main(String[] args) {
        new Main().start();
    }

    @SneakyThrows
    public void start() {
        // Document doc = Jsoup.connect("https://etalonline.by/document/?regnum=HK9900295&q_id=").get();
        Document doc = Jsoup.parse(getClass().getClassLoader().getResourceAsStream("kodeks.html"), "UTF-8", "");
        Elements elements = doc.selectFirst(".Section1").children();
        UPK upk = new UPK(buildCodexParts(elements));
        validateUPK(upk);
        log.info("Done.");
    }

    private List<CodexPart> buildCodexParts(Elements elements) {
        var codexParts = new ArrayList<CodexPart>();

        for (int i = 0; i < elements.size(); i++) {
            var element = elements.get(i);
            if (isCodexPart(element)) {
                String[] split = element.html().split("<br>");
                var codexSections = buildSections(elements, i + 1);
                i = codexSections.getNewOffset(); // skip passed lines
                codexParts.add(CodexPart.builder()
                        .numberId(stripHTML(split[0]).substring(5).trim())
                        .title(stripHTML(split[1]))
                        .sections(codexSections.getList())
                        .build());
            }
        }

        return codexParts;
    }

    private NodeResult<Section> buildSections(Elements elements, int offset) {
        int newOffset = offset;
        var sections = new ArrayList<Section>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isSection(element)) {
                String name = stripHTML(element.html().split("<br>")[1]);
                var sectionChapters = buildChapters(elements, i + 1);
                i = sectionChapters.getNewOffset(); // skip passed lines
                sections.add(Section.builder()
                        .numberId(element.attr("id"))
                        .title(name)
                        .chapters(sectionChapters.getList())
                        .build());
            } else if (Section.isGoingUp(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            } else {
                log.info("Section - trash element {}", element);
            }
        }

        return new NodeResult<>(sections, newOffset);
    }

    private NodeResult<Chapter> buildChapters(Elements elements, int offset) {
        int newOffset = offset;
        var chapters = new ArrayList<Chapter>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isChapter(element)) {
                String name = stripHTML(element.html().split("<br>")[1]);
                var res = buildArticles(elements, i + 1);
                i = res.getNewOffset();
                chapters.add(Chapter.builder()
                        .numberId(element.attr("id"))
                        .title(name)
                        .articles(res.getList())
                        .build());
            } else if (Chapter.isGoingUp(element)) {
                newOffset = i - 1;
                break;
            } else {
                log.info("Chapter - trash element?");
            }
        }

        return new NodeResult<>(chapters, newOffset);
    }

    private NodeResult<Article> buildArticles(Elements elements, int offset) {
        int newOffset = offset;
        var articles = new ArrayList<Article>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticle(element)) {

                // case 1
                var articleText = buildArticleText(elements, i + 1);
                i = articleText.getNewOffset();

                // case 2
                var articleParagraphs = buildArticleParagraphs(elements, i + 1);
                i = articleParagraphs.getNewOffset();

                // case 3
                var articleParts = buildArticleParts(elements, i + 1);
                i = articleParts.getNewOffset();

                articles.add(Article.builder()
                        .numberId(element.attr("id"))
                        .title(element.text().replaceFirst("Статья [0-9]+.", "").trim())
                        .text(String.join("\n", articleText.getList()))
                        .articleParts(articleParts.getList())
                        .articleParagraphs(articleParagraphs.getList())
                        .build());
            } else if (Article.isGoingUp(element)) {
                newOffset = i - 1;
                break;
            }
        }

        return new NodeResult<>(articles, newOffset);
    }

    private NodeResult<String> buildArticleText(Elements elements, int offset) {
        int newOffset = offset; // might be a miss
        var list = new ArrayList<String>();
        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticleText(element)) {
                list.add(element.text());
            } else {
                newOffset = i - 1;
                break;
            }
        }
        return new NodeResult<>(list, newOffset);
    }

    private NodeResult<ArticleParagraph> buildArticleParagraphs(Elements elements, int offset) {
        int newOffset = offset - 1; // might be a miss
        var list = new ArrayList<ArticleParagraph>();
        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticleParagraph(element)) {
                list.add(ArticleParagraph.builder()
                        .numberId(element.attr("id"))
                        .title(element.text().replaceFirst("[0-9]+.", "").trim())
                        .build());
            } else if (ArticleParagraph.isGoingUp(element)) {
                newOffset = i - 1;
                break;
            }
        }
        return new NodeResult<>(list, newOffset);
    }

    private NodeResult<ArticlePart> buildArticleParts(Elements elements, int offset) {
        int newOffset = offset;
        var list = new ArrayList<ArticlePart>();
        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticlePart(element)) {
                var articleParagraphs = buildArticleParagraphs(elements, i);
                i = articleParagraphs.getNewOffset();
                list.add(ArticlePart.builder()
                        .numberId(element.attr("id"))
                        .title(element.text().replaceFirst("[0-9]+.", "").trim())
                        .articleParagraphs(articleParagraphs.getList())
                        .build());
            } else if (ArticlePart.isGoingUp(element)) {
                newOffset = i - 1;
                break;
            }
        }
        return new NodeResult<>(list, newOffset);
    }

    @Data
    @RequiredArgsConstructor
    private static class NodeResult<T> {
        private final List<T> list;
        private final Integer newOffset;
    }

}
