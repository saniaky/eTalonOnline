package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Пункт статьи (aka ")")
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Text extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.TEXT;
    }

}
