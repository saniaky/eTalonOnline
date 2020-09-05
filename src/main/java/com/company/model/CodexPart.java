package com.company.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Часть
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CodexPart extends Node {

    List<Section> sections;

    @Override
    public String getType() {
        return "Часть";
    }

}
