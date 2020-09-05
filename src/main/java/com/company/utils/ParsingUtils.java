package com.company.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public final class ParsingUtils {

    private ParsingUtils() {
    }

    public static String stripHTML(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean isEmptyElement(Element element) {
        return element.text().isBlank();
    }

    public static boolean isEnd(Element element) {
        return "titlep".equals(element.className());
    }

    public static boolean isCodexChange(Element element) {
        return "changeadd".equals(element.className()) && !element.text().isBlank();
    }

    /**
     * Example html:
     * <p class="part" style="">ОБЩАЯ ЧАСТЬ</p>
     *
     * <p class="contenttext" style="">ОБЩАЯ ЧАСТЬ</p>
     *
     *
     * <p class="zagrazdel" id="І" style="">
     * <a name="РАЗДЕЛ1"></a>ОБЩАЯ ЧАСТЬ<br>РАЗДЕЛ І<br>
     * ОСНОВНЫЕ ПОЛОЖЕНИЯ<br> УГОЛОВНО-ИСПОЛНИТЕЛЬНОГО ЗАКОНОДАТЕЛЬСТВА РЕСПУБЛИКИ БЕЛАРУСЬ
     * </p>
     */
    public static boolean isCodexPart(Element element) {
        String text = element.text();
        String className = element.className();
        return "part".equals(className) && !text.isBlank()
                || ("zagrazdel".equals(className) && text.contains("ЧАСТЬ"))
                || ("contenttext".equals(className) && text.contains("ЧАСТЬ"));
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

    public static boolean isArticleText(Element element) {
        return "newncpi".equals(element.className());
    }

    public static boolean isArticlePart(Element element) {
        return ("point".equals(element.className()) || "underpoint".equals(element.className()))
                && !isEmptyElement(element)
                && element.text().replaceFirst("[0-9]+", "").trim().charAt(0) == '.';
    }

    public static boolean isArticleParagraph(Element element) {
        return ("point".equals(element.className()) || "underpoint".equals(element.className()))
                && !isEmptyElement(element)
                && element.text().replaceFirst("[0-9]+", "").trim().charAt(0) == ')';
    }

    public static boolean isComment(Element element) {
        return "comment".equals(element.className());
    }

    public static boolean isNote(Element element) {
        return "snoskiline".equals(element.className()) || "snoski".equals(element.className());
    }

}
