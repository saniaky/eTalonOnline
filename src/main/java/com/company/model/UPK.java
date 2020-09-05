package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


/**
 * Уголовно-процессуальный кодекс
 * <p>
 * Иерархия:
 * Часть раздела (Part) -> Раздел (Section) -> Глава (Chapter) -> Статья -> (Часть Статьи -> Пункт статьи) или (Пункт статьи)
 */
@Data
@Builder
@AllArgsConstructor
public class UPK {

    List<CodexPart> codexParts;

}
