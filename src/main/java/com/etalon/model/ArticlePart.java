package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Часть статьи  (".")
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ArticlePart extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.ARTICLE;
    }

}
