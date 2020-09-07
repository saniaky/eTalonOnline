package com.etalon.utils;

import com.etalon.model.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.etalon.model.CodexNodeType.*;
import static com.etalon.utils.ParsingUtils.stripHTML;

public final class CodexNodeBuilder {

    private CodexNodeBuilder() {
    }

    public static CodexNode buildNode(CodexNodeType nodeType, Element element) {
        String text = element.text().trim();
        if (CHANGE == nodeType) {
            return CodexChange.builder()
                    .id(text.substring(text.indexOf("<") + 1, text.indexOf(">")))
                    .link(element.selectFirst("a").attr("href"))
                    .title(text)
                    .build();
        }
        if (CODEX_PART == nodeType) {
            String[] split = element.html().split("<br>");
            return CodexPart.builder()
                    .id(split[0].toUpperCase().trim())
                    .title(split[1].toUpperCase().trim())
                    .build();
        }
        if (SECTION == nodeType) {
            return Section.builder()
                    .id(element.attr("id"))
                    .title(element.html().split("<br>")[1].trim())
                    .text(text)
                    .build();
        }
        if (CHAPTER == nodeType) {
            String title = stripHTML(element.html().split("<br>")[1]);
            String id = element.attr("id");
            return Chapter.builder()
                    .id(id)
                    .title(title)
                    .text(text)
                    .build();
        }
        if (ARTICLE == nodeType) {
            return Article.builder()
                    .id(element.attr("id"))
                    .title(stripHTML(element.html().split("<br>")[1]).toUpperCase())
                    .build();
        }
        throw new IllegalArgumentException();
    }

    public static String getUniqueElement(Elements entries, CodexNodeType nodeType) {
        return entries.select(nodeType.getCSSSelector()).text();
    }

}
