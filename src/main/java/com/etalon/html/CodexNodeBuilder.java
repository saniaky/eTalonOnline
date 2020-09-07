package com.etalon.html;

import com.etalon.model.*;
import org.jsoup.nodes.Element;

import static com.etalon.html.ParsingUtils.removeNumberPrefix;
import static com.etalon.html.ParsingUtils.stripHTML;
import static com.etalon.model.CodexNodeType.*;

public final class CodexNodeBuilder {

    private CodexNodeBuilder() {
    }

    public static CodexNode buildNode(CodexNodeType nodeType, Element element) {
        String text = element.text().trim();
        if (CHANGE_ENTRY == nodeType) {
            return CodexChange.builder()
                    .id(text.substring(text.indexOf("<") + 1, text.indexOf(">")))
                    .link(element.selectFirst("a").attr("href"))
                    .title(text)
                    .build();
        }
        if (CODEX_PART == nodeType) {
            var id = text;
            var title = text;
            var split = element.html().split("<br>");
            if (split.length > 1) {
                id = split[0];
                title = split[1];
            }
            return CodexPart.builder()
                    .id(id.toUpperCase().trim())
                    .title(title.toUpperCase().trim())
                    .build();
        }
        if (SECTION == nodeType) {
            var title = text;
            var split = element.html().split("<br>");
            if (split.length > 1) {
                title = split[1];
            }
            return Section.builder()
                    .id(element.id())
                    .title(title.trim())
                    .text(text)
                    .build();
        }
        if (CHAPTER == nodeType) {
            var title = element.text();
            var split = element.html().split("<br>");
            if (split.length > 1) {
                title = split[1].trim();
            }
            return Chapter.builder()
                    .id(element.id())
                    .title(stripHTML(title))
                    .text(text)
                    .build();
        }
        if (ARTICLE == nodeType) {
            return Article.builder()
                    .id(element.id())
                    .title(text)
                    .build();
        }
        if (ARTICLE_PART == nodeType) {
            return ArticlePart.builder()
                    .id(element.id())
                    .text(removeNumberPrefix(text))
                    .build();
        }
        if (ARTICLE_PARAGRAPH == nodeType) {
            return ArticleParagraph.builder()
                    .id(element.id())
                    .text(removeNumberPrefix(text))
                    .build();
        }
        if (ARTICLE_COMMENT == nodeType) {
            return ArticleComment.builder()
                    .text(text)
                    .build();
        }
        if (TEXT == nodeType) {
            return Text.builder()
                    .text(text)
                    .build();
        }
        throw new IllegalArgumentException("No ideas how to build such node.");
    }

}
