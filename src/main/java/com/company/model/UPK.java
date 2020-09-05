package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


/**
 * Уголовно-процессуальный кодекс
 * <p>
 * Иерархия:
 * Часть (Part) -> Раздел (Section) -> Глава (Chapter) -> Статья -> (Часть Статьи -> Пункт статьи) или (Пункт статьи)
 * <p>
 * Части статей и примечаний (за исключением имеющих одну часть) в настоящем Кодексе нумеруются арабскими цифрами с точкой,
 * пункты частей статей – арабскими цифрами со скобкой.
 */
@Data
@Builder
@AllArgsConstructor
public class UPK {

    List<CodexPart> codexParts;

}
