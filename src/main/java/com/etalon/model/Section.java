package com.etalon.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Раздел (части кодекса)
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Section extends CodexNode {

    @Override
    public CodexNodeType getType() {
        return CodexNodeType.SECTION;
    }

}
