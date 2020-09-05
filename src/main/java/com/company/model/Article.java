package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.company.utils.ParsingUtils.*;

/**
 * Статья
 *
 * <p>
 * Части статей и примечаний (за исключением имеющих одну часть) в настоящем Кодексе нумеруются арабскими цифрами с точкой,
 * пункты частей статей – арабскими цифрами со скобкой.
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Article extends Node {

    String text; // optional
    List<ArticlePart> articleParts; // Часть статьи "<number>.", optional
    List<ArticleParagraph> articleParagraphs; // Пункт статьи "<number>)" optional

    @Override
    public String getType() {
        return "Статья";
    }

    public static boolean isGoingUp(Element element) {
        return isEnd(element) || isCodexPart(element) || isSection(element) || isChapter(element);
    }

}
