package com.etalon;

import com.etalon.model.CodexBook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppWaterTest {

    static final App app = new App();

    @Test
    void forest() throws Exception {
        // Given
        String codexId = "hk1400149";
        String codexName = "Водный кодекс Республики Беларусь";

        // When
        CodexBook codex = app.createCodex(codexId, codexName);

        // Then
        assertEquals(1, codex.getCodexChanges().size()); // Кол-во правок в кодексе
        assertEquals(24, codex.getChildren().size()); // Кол-во глав в кодексе
    }

}
