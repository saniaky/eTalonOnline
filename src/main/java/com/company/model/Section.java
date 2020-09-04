package com.company.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Раздел
 */
@Data
@Builder
public class Section {

    String number;
    String name;
    List<Chapter> chapters;

}
