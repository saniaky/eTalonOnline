package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Изменения и дополнения
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CodexChange extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.CHANGE;
    }

}
