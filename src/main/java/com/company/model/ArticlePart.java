package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.company.ParsingUtils.*;

/**
 * Часть статьи  (".")
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ArticlePart extends Node {

    List<ArticleParagraph> articleParagraphs; // Часть статьи "." => Пункт статьи ")"

    public static boolean isGoingUp(Element element) {
        return isEnd(element) || isCodexPart(element) || isSection(element) || isChapter(element) || isArticlePart(element);
    }

    @Override
    public String getType() {
        return "Часть статьи";
    }

}
