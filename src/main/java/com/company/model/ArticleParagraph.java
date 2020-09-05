package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jsoup.nodes.Element;

import static com.company.utils.ParsingUtils.*;

/**
 * Пункт статьи (aka ")")
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ArticleParagraph extends Node {

    public static boolean isGoingUp(Element element) {
        return isEnd(element) || isCodexPart(element) || isSection(element)
                || isChapter(element) || isArticle(element) || isArticlePart(element);
    }

    @Override
    public String getType() {
        return "Пункт";
    }

}
