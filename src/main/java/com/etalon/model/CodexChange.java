package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Изменения и дополнения
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CodexChange extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.CHANGE_ENTRY;
    }

}
