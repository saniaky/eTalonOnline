package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Глава
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Chapter extends Node {

    List<Article> articles;

    @Override
    public String getType() {
        return "Глава";
    }

}
