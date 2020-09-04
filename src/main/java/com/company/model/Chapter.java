package com.company.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Глава
 */
@Data
@Builder
public class Chapter {

    String number;
    String name;
    List<Article> articles;

}
