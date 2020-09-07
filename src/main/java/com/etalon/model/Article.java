package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Статья
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Article extends CodexNode {

    private String comment;

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.ARTICLE;
    }

}
