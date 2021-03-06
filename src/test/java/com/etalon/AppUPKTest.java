package com.etalon;

import com.etalon.model.Article;
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

        article1(codex);
        article60(codex);
        article70(codex);
    }

    private void article70(CodexBook codex) {
        /*
            Часть первая
            РАЗДЕЛ II ГОСУДАРСТВЕННЫЕ ОРГАНЫ И ДРУГИЕ УЧАСТНИКИ УГОЛОВНОГО ПРОЦЕССА
            ГЛАВА 8 МЕРЫ ПО ОБЕСПЕЧЕНИЮ БЕЗОПАСНОСТИ УЧАСТНИКОВ УГОЛОВНОГО ПРОЦЕССА И ДРУГИХ ЛИЦ
                Статья 70. Личная охрана, охрана жилища и имущества
                    Обеспечение безопасности в виде личной охраны, охраны жилища и имущества защищаемого лица осуществляют органы внутренних дел и государственной безопасности по постановлению (определению) органа, ведущего уголовный процесс.
         */
        var article = (Article) codex.getChildren().get(0).getChildren().get(1).getChildren().get(4).getChildren().get(5);
        assertEquals(1, article.getChildren().size());
        String expected = "Обеспечение безопасности в виде личной охраны, охраны жилища и имущества защищаемого лица осуществляют органы внутренних дел и государственной безопасности по постановлению (определению) органа, ведущего уголовный процесс.";
        assertEquals(expected, article.getChildren().get(0).getText());
    }

    private void article60(CodexBook codex) {
        // Часть первая
        // РАЗДЕЛ II ГОСУДАРСТВЕННЫЕ ОРГАНЫ И ДРУГИЕ УЧАСТНИКИ УГОЛОВНОГО ПРОЦЕССА
        //   ГЛАВА 7 ИНЫЕ УЧАСТНИКИ УГОЛОВНОГО ПРОЦЕССА
        //     Статья 60/1. Адвокат свидетеля
        var article = (Article) codex.getChildren().get(0).getChildren().get(1).getChildren().get(3).getChildren().get(1);
        assertEquals(4, article.getChildren().size());
        assertEquals(5, article.getChildren().get(0).getChildren().size());
        assertEquals(0, article.getChildren().get(1).getChildren().size());
        assertEquals(3, article.getChildren().get(2).getChildren().size());
        assertEquals(0, article.getChildren().get(3).getChildren().size());
    }

    private void article1(CodexBook codex) {
        Article article1 = (Article) codex.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0);
        assertEquals(5, article1.getChildren().size());
        String expected = "Уголовно-процессуальный кодекс Республики Беларусь, основываясь на Конституции Республики Беларусь, устанавливает порядок деятельности органов, ведущих уголовный процесс, а также права и обязанности участников уголовного процесса.";
        assertEquals(expected, article1.getChildren().get(0).getText());
    }

    // ЧАСТЬ ПЕРВАЯ.
    private void part1(CodexBook codex) {
        CodexNode part1 = codex.getChildren().get(0);
        assertEquals(6, part1.getChildren().size()); // 6 разделов

        // Раздел 1
        //  ГЛАВА 1. УГОЛОВНО-ПРОЦЕССУАЛЬНОЕ ЗАКОНОДАТЕЛЬСТВО
        //  ...
        //  ГЛАВА 3. УГОЛОВНОЕ ПРЕСЛЕДОВАНИЕ
        CodexNode section1 = part1.getChildren().get(0);
        assertEquals("ОСНОВНЫЕ ПОЛОЖЕНИЯ", section1.getTitle());
        assertEquals(3, section1.getChildren().size()); // 3 главы
        assertEquals("УГОЛОВНО-ПРОЦЕССУАЛЬНОЕ ЗАКОНОДАТЕЛЬСТВО", section1.getChildren().get(0).getTitle());
        assertEquals("УГОЛОВНОЕ ПРЕСЛЕДОВАНИЕ", section1.getChildren().get(2).getTitle());

        // Раздел 6
        //   ГЛАВА 17. Гражданский иск в уголовном процессе
        //   ...
        //   ГЛАВА 19. Соединение, выделение и восстановление уголовных дел
        CodexNode section6 = part1.getChildren().get(5);
        assertEquals("ИНЫЕ ОБЩИЕ ПОЛОЖЕНИЯ", section6.getTitle());
        assertEquals(3, section6.getChildren().size()); // 3 главы
        assertEquals("ГРАЖДАНСКИЙ ИСК В УГОЛОВНОМ ПРОЦЕССЕ", section6.getChildren().get(0).getTitle());
        assertEquals("СОЕДИНЕНИЕ, ВЫДЕЛЕНИЕ И ВОССТАНОВЛЕНИЕ УГОЛОВНЫХ ДЕЛ", section6.getChildren().get(2).getTitle());
    }

    //  ЧАСТЬ ВТОРАЯ. ДОСУДЕБНОЕ ПРОИЗВОДСТВО
    private void part2(CodexBook codex) {
        CodexNode part2 = codex.getChildren().get(1);
        assertEquals(2, part2.getChildren().size()); // 2 раздела

        //  РАЗДЕЛ VII. ВОЗБУЖДЕНИЕ УГОЛОВНОГО ДЕЛА
        assertEquals("ВОЗБУЖДЕНИЕ УГОЛОВНОГО ДЕЛА", part2.getChildren().get(0).getTitle());

        //  РАЗДЕЛ VIII. ПРЕДВАРИТЕЛЬНОЕ РАССЛЕДОВАНИЕ
        assertEquals("ПРЕДВАРИТЕЛЬНОЕ РАССЛЕДОВАНИЕ", part2.getChildren().get(1).getTitle());
    }

    // ЧАСТЬ ТРЕТЬЯ. СУДЕБНОЕ ПРОИЗВОДСТВО
    private void part3(CodexBook codex) {
        CodexNode part3 = codex.getChildren().get(2);
        assertEquals(8, part3.getChildren().size()); // 8 разделов

        //  РАЗДЕЛ IX. ПРОИЗВОДСТВО В СУДЕ ПЕРВОЙ ИНСТАНЦИИ
        assertEquals("ПРОИЗВОДСТВО В СУДЕ ПЕРВОЙ ИНСТАНЦИИ", part3.getChildren().get(0).getTitle());

        //  РАЗДЕЛ XVI. ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ
        assertEquals("ЗАКЛЮЧИТЕЛЬНЫЕ ПОЛОЖЕНИЯ", part3.getChildren().get(7).getTitle());
    }

}
