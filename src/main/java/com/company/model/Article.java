package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    String intro; // optional
    List<ArticleParagraph> articleParagraphs; // Пункт статьи "<number>)" optional
    List<ArticlePart> articleParts; // Часть статьи "<number>.", optional

    @Override
    public String getType() {
        return "Статья";
    }

}
