package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Текстовый элемент
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Text extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.TEXT;
    }

}
