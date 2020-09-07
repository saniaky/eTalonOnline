package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Комментарий к статье
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ArticleComment extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.ARTICLE_COMMENT;
    }

}
