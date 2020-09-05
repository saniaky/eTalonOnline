package com.company.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.company.ParsingUtils.isCodexPart;
import static com.company.ParsingUtils.isEnd;

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

    public static boolean isGoingUp(Element element) {
        return isEnd(element) || isCodexPart(element);
    }

}
