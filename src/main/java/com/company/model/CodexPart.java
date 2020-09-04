package com.company.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Часть
 */
@Data
@Builder
public class CodexPart {

    String number;
    String name;
    List<Section> sections;

}
