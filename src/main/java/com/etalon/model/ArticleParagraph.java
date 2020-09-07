package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Пункт статьи  (")")
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArticleParagraph extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.ARTICLE_PART;
    }

}
