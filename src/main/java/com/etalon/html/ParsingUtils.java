package com.etalon.html;

import com.etalon.model.CodexNodeType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.etalon.model.CodexNodeType.*;
import static java.util.Map.entry;

public final class ParsingUtils {

    public static final String NUMBER_REGEX = "[0-9]+";

    private ParsingUtils() {
    }

    public static boolean isKnown(String className) {
        return Set.of(CSSClassName.values()).stream().anyMatch(e -> e.getClassName().equals(className));
    }

    public static String stripHTML(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean notEmpty(Element element) {
        return !isEmpty(element);
    }

    public static boolean isEmpty(Element element) {
        return element.text().isBlank();
    }

    public static boolean isEnd(Element element) {
        return "table".equals(element.tagName())
                || CSSClassName.TITLEP.getClassName().equals(element.className());
    }

    public static boolean isArticlePart(Element element) {
        return (cssFit(CSSClassName.POINT, element) || cssFit(CSSClassName.UNDERPOINT, element))
                && element.text().replaceFirst(NUMBER_REGEX, "").trim().charAt(0) == '.';
    }

    public static boolean isArticleParagraph(Element element) {
        return (cssFit(CSSClassName.POINT, element) || cssFit(CSSClassName.UNDERPOINT, element))
                && element.text().replaceFirst(NUMBER_REGEX, "").trim().charAt(0) == ')';
    }

    private static boolean isText(Element element) {
        return cssFit(CSSClassName.TEXT, element);
    }

    public static String removeNumberPrefix(String text) {
        return text.replaceFirst(NUMBER_REGEX, "").trim().substring(1).trim();
    }

    private static boolean cssFit(CSSClassName e, Element element) {
        return Objects.equals(e.getClassName(), element.className());
    }

    public static boolean isFit(Element element, CodexNodeType nodeType) {
        if (isEnd(element) || isEmpty(element)) {
            return false;
        }
        switch (nodeType) {
            case CODEX_NAME:
                return cssFit(CSSClassName.CODEX_NAME, element);
            case CODEX_DATE:
                return cssFit(CSSClassName.CODEX_DATE, element);
            case CODEX_NUMBER:
                return cssFit(CSSClassName.CODEX_NUMBER, element);
            case CONTENT_ENTRY:
                return cssFit(CSSClassName.CONTENT_TEXT, element);
            case NOTE:
                return cssFit(CSSClassName.NOTE, element);
            case CHANGE_ENTRY:
                return cssFit(CSSClassName.CHANGE_TEXT, element);
            case CODEX_ACCEPTED:
                return cssFit(CSSClassName.CODEX_ACCEPTED, element);
            case CODEX_PART:
                return cssFit(CSSClassName.CODEX_PART, element);
            case SECTION:
                return cssFit(CSSClassName.SECTION, element);
            case CHAPTER:
                return cssFit(CSSClassName.CHAPTER, element);
            case ARTICLE:
                return cssFit(CSSClassName.ARTICLE, element);
            case ARTICLE_PART:
                return isArticlePart(element);
            case ARTICLE_PARAGRAPH:
                return isArticleParagraph(element);
            case ARTICLE_COMMENT:
                return cssFit(CSSClassName.ARTICLE_COMMENT, element);
            case TEXT:
                return isText(element);
            default:
                return false;
        }
    }

    public static boolean isGoingUp(Element element, CodexNodeType nodeType) {
        if (isEnd(element)) {
            return true;
        }
        switch (nodeType) {
            case CHANGE_ENTRY:
                return cssFit(CSSClassName.CONTENT_TEXT, element)
                        || cssFit(CSSClassName.CODEX_PART, element);
            case CODEX_PART:
                return isEnd(element);
            case SECTION:
                return cssFit(CSSClassName.CODEX_PART, element);
            case CHAPTER:
                return isGoingUp(element, SECTION)
                        || cssFit(CSSClassName.SECTION, element);
            case ARTICLE:
                return isGoingUp(element, CHAPTER)
                        || cssFit(CSSClassName.CHAPTER, element);
            case ARTICLE_PART:
                return isGoingUp(element, ARTICLE)
                        || cssFit(CSSClassName.ARTICLE, element)
                        || cssFit(CSSClassName.ARTICLE_COMMENT, element);
            case ARTICLE_PARAGRAPH:
                return isGoingUp(element, ARTICLE_PART) || isArticlePart(element);
            case TEXT:
                return !isText(element);
            default:
                return true;
        }
    }

    public static String getUniqueValue(Elements entries, CodexNodeType nodeType) {
        var cssMappings = Map.ofEntries(
                entry(CODEX_DATE, CSSClassName.CODEX_DATE.getClassName()),
                entry(CODEX_NUMBER, CSSClassName.CODEX_NUMBER.getClassName()),
                entry(CODEX_ACCEPTED, CSSClassName.CODEX_ACCEPTED.getClassName())
        );
        String cssSelector = cssMappings.get(nodeType);
        if (cssSelector == null) {
            throw new IllegalArgumentException("Such type is not unique and you can't fetch it using this method.");
        }
        return entries.select(cssSelector).text();
    }

}
