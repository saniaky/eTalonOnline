package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Часть статьи  (".")
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArticlePart extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.ARTICLE_PART;
    }

}
