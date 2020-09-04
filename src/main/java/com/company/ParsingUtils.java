package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public final class ParsingUtils {

    public static String stripHTML(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean isEmptyElement(Element element) {
        return element.text().isBlank();
    }

    public static boolean isCodexPart(Element element) {
        return "part".equals(element.className()) && !element.text().isBlank();
    }

    public static boolean isSection(Element element) {
        return ("zagrazdel".equals(element.className()) && !element.text().isBlank());
    }

    public static boolean isChapter(Element element) {
        return "chapter".equals(element.className()) && !element.text().isBlank();
    }

    public static boolean isArticle(Element element) {
        return "article".equals(element.className()) && !element.text().isBlank();
    }

    public static boolean isArticlePart(Element element) {
        return "newncpi".equals(element.className());
    }

    public static boolean isArticleParagraph(Element element) {
        return "point".equals(element.className()) || "underpoint".equals(element.className());
    }

    public static boolean isComment(Element element) {
        return "comment".equals(element.className());
    }

    public static boolean isNote(Element element) {
        return "snoskiline".equals(element.className()) || "snoski".equals(element.className());
    }

}
