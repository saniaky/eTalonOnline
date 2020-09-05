package com.company.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Раздел
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Section extends Node {

    List<Chapter> chapters;

    @Override
    public String getType() {
        return "РАЗДЕЛ";
    }

}
