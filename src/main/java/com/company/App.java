package com.company;

import com.company.model.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.company.utils.CodexManager.getCodexHTML;
import static com.company.utils.CodexManager.saveCodexJSON;
import static com.company.utils.ParsingUtils.*;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException {
        var main = new App();
        Document doc = Jsoup.connect("https://etalonline.by/kodeksy/").get();
        Elements codexEntries = doc.selectFirst(".faq-a").select("a");
        for (Element codex : codexEntries) {
            main.createCodex(codex);
        }
    }

    public void createCodex(Element codexLink) throws IOException {
        String codexId = codexLink.attr("href").replace("/document/?regnum=", "").split("&")[0];
        String codexName = codexLink.text();
        var doc = getCodexHTML(codexId);
        var elements = doc.selectFirst(".Section1").children();
        var changes = buildChanges(elements);

        // Pre-processing
        // remove empty tags
        // remove Приложение (table)
        // verify structure: part -> section -> chapter -> ..., fix if possible

        Codex codex = Codex.builder()
                .id(codexId)
                .name(codexName)
                .codexChanges(changes.getList())
                .codexParts(buildCodexParts(elements, changes.getNewOffset()))
                .build();
        saveCodexJSON(codex);
    }

    private NodeResult<CodexChange> buildChanges(Elements elements) {
        var codexChanges = new ArrayList<CodexChange>();
        int newOffset = 0;
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isCodexChange(element)) {
                var text = element.text();
                codexChanges.add(CodexChange.builder()
                        .link(element.selectFirst("a").attr("href"))
                        .number(text.substring(text.indexOf("<") + 1, text.indexOf(">")))
                        .name(text)
                        .build());
            } else if ("contentword".equals(element.className())) {
                newOffset = i;
                break;
            }
        }
        return new NodeResult<>(codexChanges, newOffset);
    }

    private List<CodexPart> buildCodexParts(Elements elements, int offset) {
        var codexParts = new ArrayList<CodexPart>();
        for (int i = offset; i < elements.size(); i++) {
            var element = elements.get(i);
            if (isCodexPart(element)) {
                String text = element.text();
                var codexSections = buildSections(elements, i + 1);
                i = codexSections.getNewOffset();
                codexParts.add(CodexPart.builder()
                        .numberId(text)
                        .title(text)
                        .sections(codexSections.getList())
                        .build());
            }
        }
        return codexParts;
    }

    private NodeResult<Section> buildSections(Elements elements, int offset) {
        int newOffset = offset;
        var list = new ArrayList<Section>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isSection(element)) {
                String name = stripHTML(element.html().split("<br>")[1]);
                var sectionChapters = buildChapters(elements, i + 1);
                i = sectionChapters.getNewOffset(); // skip passed lines
                list.add(Section.builder()
                        .numberId(element.attr("id"))
                        .title(name.toUpperCase())
                        .chapters(sectionChapters.getList())
                        .build());
            } else if (Section.isGoingUp(element)) { // if going up - stop
                newOffset = i - 1;
                break;
            } else if (!isEmptyElement(element)) {
                log.info("Section - trash? {}", element);
            }
        }

        return new NodeResult<>(list, newOffset);
    }

    private NodeResult<Chapter> buildChapters(Elements elements, int offset) {
        int newOffset = offset;
        var list = new ArrayList<Chapter>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isChapter(element)) {
                String name = stripHTML(element.html().split("<br>")[1]);
                var res = buildArticles(elements, i + 1);
                i = res.getNewOffset();
                list.add(Chapter.builder()
                        .numberId(element.attr("id"))
                        .title(name)
                        .articles(res.getList())
                        .build());
            } else if (Chapter.isGoingUp(element)) {
                newOffset = i - 1;
                break;
            } else if (!isEmptyElement(element)) {
                log.info("Chapter - trash? {}", element);
            }
        }

        return new NodeResult<>(list, newOffset);
    }

    private NodeResult<Article> buildArticles(Elements elements, int offset) {
        int newOffset = offset;
        var list = new ArrayList<Article>();

        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticle(element)) {
                // case 1
                var articleText = buildArticleText(elements, i + 1);
                i = articleText.getNewOffset();

                // case 2
                var articleParts = buildArticleParts(elements, i + 1);
                i = articleParts.getNewOffset();

                // case 3
                var articleParagraphs = buildArticleParagraphs(elements, i + 1);
                i = articleParagraphs.getNewOffset();

                list.add(Article.builder()
                        .numberId(element.attr("id"))
                        .title(element.text().replaceFirst("Статья [0-9]+.", "").trim())
                        .text(String.join("\n", articleText.getList()))
                        .articleParts(articleParts.getList())
                        .articleParagraphs(articleParagraphs.getList())
                        .build());
            } else if (Article.isGoingUp(element)) {
                newOffset = i - 1;
                break;
            } else if (!isEmptyElement(element)) {
                log.info("Article - trash? {}", element);
            }
        }

        return new NodeResult<>(list, newOffset);
    }

    private NodeResult<String> buildArticleText(Elements elements, int offset) {
        int newOffset = offset;
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

    private NodeResult<ArticlePart> buildArticleParts(Elements elements, int offset) {
        int newOffset = offset;
        var list = new ArrayList<ArticlePart>();
        for (int i = offset; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (isArticlePart(element)) {
                var articleParagraphs = buildArticleParagraphs(elements, i + 1);
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

    private NodeResult<ArticleParagraph> buildArticleParagraphs(Elements elements, int offset) {
        int newOffset = offset;
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

    @Data
    @RequiredArgsConstructor
    private static class NodeResult<T> {
        private final List<T> list;
        private final Integer newOffset;
    }

}
