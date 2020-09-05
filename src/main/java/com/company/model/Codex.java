package com.company.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Иерархия:
 * Часть раздела (Part) -> Раздел (Section) -> Глава (Chapter) -> Статья -> (Часть Статьи -> Пункт статьи) или (Пункт статьи)
 */
@Data
@Builder
public class Codex {

    String id;
    String name;
    List<CodexChange> codexChanges;
    List<CodexPart> codexParts;

    public String getLink() {
        return "http://etalonline.by/document/?regnum=" + id;
    }

}
