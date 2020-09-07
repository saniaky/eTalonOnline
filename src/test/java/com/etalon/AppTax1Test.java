package com.etalon;

import com.etalon.model.CodexBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTax1Test {

    static final App app = new App();

    @Test
    void forest() throws Exception {
        // Given
        String codexId = "hk0200166";
        String codexName = "Налоговый кодекс Республики Беларусь (Общая часть)";

        // When
        CodexBook codex = app.createCodex(codexId, codexName);

        // Then
        assertEquals(1, codex.getCodexChanges().size()); // Кол-во правок в кодексе
        assertEquals(24, codex.getChildren().size()); // Кол-во глав в кодексе
    }

}
