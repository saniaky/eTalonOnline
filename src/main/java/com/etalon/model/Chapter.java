package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Глава
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Chapter extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.CHAPTER;
    }

}
