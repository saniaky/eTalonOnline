package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public final class ParsingUtils {

    public static String stripHTML(String html) {
        return Jsoup.parse(html).text();
    }

    public static boolean isCodexPart(Element element) {
        return "part".equals(element.className()) && !element.text().isBlank();
    }

    public static boolean isSection(Element element) {
        return "zagrazdel".equals(element.className()) && !element.text().isBlank();
    }

    public static boolean isChapter(Element element) {
        return "chapter".equals(element.className()) && !element.text().isBlank();
    }

}
