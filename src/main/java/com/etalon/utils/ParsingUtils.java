package com.etalon.utils;

import com.etalon.model.CodexNodeType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Set;

import static com.etalon.model.CodexNodeType.*;

public final class ParsingUtils {

    private ParsingUtils() {
    }

    public static boolean isKnown(String className) {
        return Set.of(CodexNodeType.values()).stream().anyMatch(e -> e.getClassNames().contains(className));
    }

    public static String stripHTML(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean notEmpty(Element element) {
        return !isBlank(element);
    }

    public static boolean isBlank(Element element) {
        return element.text().isBlank();
    }

    public static boolean isEnd(Element element) {
        return "table".equals(element.tagName()) || "titlep".equals(element.className());
    }

    public static boolean isCodexChange(Element element) {
        return CHANGE.getClassNames().contains(element.className());
    }

    public static boolean isContentTitle(Element element) {
        return CONTENT_TITLE.getClassNames().contains(element.className());
    }

    public static boolean isCodexPart(Element element) {
        return CODEX_PART.getClassNames().contains(element.className());
    }

    public static boolean isSection(Element element) {
        return SECTION.getClassNames().contains(element.className());
    }

    public static boolean isChapter(Element element) {
        return CHAPTER.getClassNames().contains(element.className()) && notEmpty(element);
    }

    public static boolean isArticle(Element element) {
        return ARTICLE.getClassNames().contains(element.className()) && notEmpty(element);
    }

    public static boolean isArticleText(Element element) {
        return "newncpi".equals(element.className());
    }

    public static boolean isArticlePart(Element element) {
        return ("point".equals(element.className()) || "underpoint".equals(element.className()))
                && notEmpty(element)
                && element.text().replaceFirst("[0-9]+", "").trim().charAt(0) == '.';
    }

    public static boolean isArticleParagraph(Element element) {
        return ("point".equals(element.className()) || "underpoint".equals(element.className()))
                && notEmpty(element)
                && element.text().replaceFirst("[0-9]+", "").trim().charAt(0) == ')';
    }

    public static boolean isComment(Element element) {
        return ARTICLE_COMMENT.getClassNames().contains(element.className());
    }

    public static boolean isNote(Element element) {
        return NOTE_LINE.getClassNames().contains(element.className())
                || NOTE.getClassNames().contains(element.className());
    }

}
