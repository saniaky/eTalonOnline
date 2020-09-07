package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Часть кодекса
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CodexPart extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.CODEX_PART;
    }

}
