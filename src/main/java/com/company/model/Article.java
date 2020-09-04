package com.company.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Статья
 *
 * <p>
 * Части статей и примечаний (за исключением имеющих одну часть) в настоящем Кодексе нумеруются арабскими цифрами с точкой,
 * пункты частей статей – арабскими цифрами со скобкой.
 */
@Data
@Builder
public class Article {

    String number;
    String name;

    String text; // optional
    List<ArticleParagraph> articleParagraphs; // Пункт статьи "<number>.", optional
    List<ArticlePart> articleParts; // Часть статьи "<number>)" optional

    // Статья 133 - только название и номер
    // Статья 147 и Статья 147/1
    // Статья 108

}
