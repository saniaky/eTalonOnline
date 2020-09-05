package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Пункт статьи
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ArticleParagraph extends Node {

    String text;
    List<ArticlePart> articleParagraphs; // optional

    @Override
    public String getType() {
        return "Пункт статьи";
    }

}
