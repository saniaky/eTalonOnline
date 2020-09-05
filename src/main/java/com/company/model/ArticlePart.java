package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Часть статьи
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ArticlePart extends Node {

    String text;

    @Override
    public String getType() {
        return "Часть статьи";
    }

}
