package com.company.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.jsoup.nodes.Element;

import java.util.List;

import static com.company.utils.ParsingUtils.*;

/**
 * Глава
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Chapter extends Node {

    List<Article> articles;

    public static boolean isGoingUp(Element element) {
        return isEnd(element) || isCodexPart(element) || isSection(element);
    }

    @Override
    public String getType() {
        return "Глава";
    }

}
