package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Статья
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Article extends CodexNode {

    String comment;

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.ARTICLE;
    }

}
