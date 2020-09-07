package com.etalon.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Кодекс РБ
 * <p>
 * Иерархия:
 * Часть раздела (Part) -> Раздел (Section) -> Глава (Chapter) -> Статья -> (Часть Статьи -> Пункт статьи) или (Пункт статьи)
 */
@Data
@Builder
@ToString(onlyExplicitlyIncluded = true)
public class CodexBook {

    @ToString.Include
    String id;

    @ToString.Include
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
