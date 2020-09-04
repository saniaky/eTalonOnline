package com.company.model;

import lombok.Data;

import java.util.List;


/**
 * Уголовно-процессуальный кодекс
 * <p>
 * Иерархия:
 * Часть (Part) -> Раздел (Section) -> Глава (Chapter) -> Статья -> (Часть Статьи -> Пункт статьи) или (Пункт статьи)
 */
@Data
public class UPK {

    List<CodexPart> codexParts;

}
