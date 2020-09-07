package com.etalon;

import com.etalon.model.CodexBook;
import com.etalon.model.CodexNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppUPKTest {

    static final App app = new App();

    @Test
    void testUPK() throws Exception {
        // Given
        String codexId = "hk9900295";
        String codexName = "Уголовно-процессуальный кодекс Республики Беларусь";

        // When
        CodexBook codex = app.createCodex(codexId, codexName);

        // Then
        assertEquals(54, codex.getCodexChanges().size()); // Кол-во правок в кодексе
        assertEquals(3, codex.getChildren().size()); // Кол-во частей кодекса
        part1(codex);
        part2(codex);
        part3(codex);
    }

    private void part1(CodexBook codex) {
        // Часть 1
        CodexNode part1 = codex.getChildren().get(0);
        assertEquals(6, part1.getChildren().size()); // Часть 1 - 6 разделов
        assertEquals("ОСНОВНЫЕ ПОЛОЖЕНИЯ", part1.getChildren().get(0).getTitle());
        assertEquals("ИНЫЕ ОБЩИЕ ПОЛОЖЕНИЯ", part1.getChildren().get(5).getTitle());
    }

    private void part2(CodexBook codex) {
        /*
            ЧАСТЬ ВТОРАЯ. ДОСУДЕБНОЕ ПРОИЗВОДСТВО
                РАЗДЕЛ VII. ВОЗБУЖДЕНИЕ УГОЛОВНОГО ДЕЛА
                РАЗДЕЛ VIII. ПРЕДВАРИТЕЛЬНОЕ РАССЛЕДОВАНИЕ

         */
        CodexNode part2 = codex.getChildren().get(1);
        assertEquals(2, part2.getChildren().size()); // Часть 3 - 2 раздела
        assertEquals("ВОЗБУЖДЕНИЕ УГОЛОВНОГО ДЕЛА", part2.getChildren().get(0).getTitle());
        assertEquals("ПРЕДВАРИТЕЛЬНОЕ РАССЛЕДОВАНИЕ", part2.getChildren().get(1).getTitle());
    }

    private void part3(CodexBook codex) {
        /*
            ЧАСТЬ ТРЕТЬЯ. СУДЕБНОЕ ПРОИЗВОДСТВО
                РАЗДЕЛ IX. ПРОИЗВОДСТВО В СУДЕ ПЕРВОЙ ИНСТАНЦИИ
                ...
                РАЗДЕЛ XVI. ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ
         */
        CodexNode part3 = codex.getChildren().get(2);
        assertEquals(8, part3.getChildren().size()); // Часть 3 - 8 разделов
        assertEquals("ПРОИЗВОДСТВО В СУДЕ ПЕРВОЙ ИНСТАНЦИИ", part3.getChildren().get(0).getTitle());
        assertEquals("ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ", part3.getChildren().get(7).getTitle());
    }

}
