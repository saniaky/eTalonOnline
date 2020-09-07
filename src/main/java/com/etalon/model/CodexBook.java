package com.etalon.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Кодекс РБ
 * <p>
 * Иерархия:
 * Часть раздела (Part) -> Раздел (Section) -> Глава (Chapter) -> Статья -> (Часть Статьи -> Пункт статьи) или (Пункт статьи)
 */
@Data
@Builder
public class CodexBook {

    String id;
    String name;
    String date;
    String number;
    String acceptedDetails;
    List<CodexNode> codexChanges;
    List<CodexNode> children;

    public String getLink() {
        return "http://etalonline.by/document/?regnum=" + id;
    }

}
