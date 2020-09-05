package com.company;

import com.company.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SizeTest {

    // Часть - кол-во Разделов
    static Map<String, Integer> expectedCodexSizes = Map.of(
            "ЧАСТЬ ПЕРВАЯ", 6,
            "ЧАСТЬ ВТОРАЯ", 2,
            "ЧАСТЬ ТРЕТЬЯ", 8
    );

    // Раздел - кол-во Глав
    static Map<String, Integer> expectedSectionSizes = Map.ofEntries(
            // ЧАСТЬ ПЕРВАЯ
            entry("РАЗДЕЛ I", 3),
            entry("РАЗДЕЛ II", 6),
            entry("РАЗДЕЛ III", 2),
            entry("РАЗДЕЛ IV", 3),
            entry("РАЗДЕЛ V", 2),
            entry("РАЗДЕЛ VI", 3),
            // ЧАСТЬ ВТОРАЯ
            entry("РАЗДЕЛ VII", 2),
            entry("РАЗДЕЛ VIII", 10),
            // ЧАСТЬ ТРЕТЬЯ
            entry("РАЗДЕЛ IX", 7),
            entry("РАЗДЕЛ X", 2),
            entry("РАЗДЕЛ XI", 1),
            entry("РАЗДЕЛ XII", 1),
            entry("РАЗДЕЛ XIII", 1),
            entry("РАЗДЕЛ XIV", 8),
            entry("РАЗДЕЛ XV", 8),
            entry("РАЗДЕЛ XVI", 1)
    );


    // Глава - кол-во Статей
    static Map<String, Integer> expectedChapterSizes = Map.ofEntries(
            // РАЗДЕЛ I. ОСНОВНЫЕ ПОЛОЖЕНИЯ
            entry("ГЛАВА 1", 6),
            entry("ГЛАВА 2", 19),
            entry("ГЛАВА 3", 5),
            // РАЗДЕЛ II. ГОСУДАРСТВЕННЫЕ ОРГАНЫ И ДРУГИЕ УЧАСТНИКИ УГОЛОВНОГО ПРОЦЕССА
            entry("ГЛАВА 4", 3),
            entry("ГЛАВА 5", 7),
            entry("ГЛАВА 6", 23),
            entry("ГЛАВА 7", 6),
            entry("ГЛАВА 8", 11),
            entry("ГЛАВА 9", 12),
            // РАЗДЕЛ III. ДОКАЗАТЕЛЬСТВА И ДОКАЗЫВАНИЕ
            entry("ГЛАВА 10", 14),
            entry("ГЛАВА 11", 5),
            entry("ГЛАВА 12", 9),
            entry("ГЛАВА 13", 12),
            entry("ГЛАВА 14", 8),
            entry("ГЛАВА 15", 3),
            entry("ГЛАВА 16", 11),
            entry("ГЛАВА 17", 10),
            entry("ГЛАВА 18", 6),
            entry("ГЛАВА 19", 3),
            entry("ГЛАВА 20", 12),
            entry("ГЛАВА 21", 0),
            entry("ГЛАВА 22", 0),
            entry("ГЛАВА 23", 0),
            entry("ГЛАВА 24", 0),
            entry("ГЛАВА 25", 0),
            entry("ГЛАВА 26", 0),
            entry("ГЛАВА 27", 0),
            entry("ГЛАВА 28", 0),
            entry("ГЛАВА 29", 0),
            entry("ГЛАВА 30", 0),
            entry("ГЛАВА 31", 0),
            entry("ГЛАВА 32", 0),
            entry("ГЛАВА 33", 0),
            entry("ГЛАВА 34", 0),
            entry("ГЛАВА 35", 0),
            entry("ГЛАВА 36", 0),
            entry("ГЛАВА 37", 0),
            entry("ГЛАВА 38", 0),
            entry("ГЛАВА 39", 0),
            entry("ГЛАВА 40", 0),
            entry("ГЛАВА 41", 0),
            entry("ГЛАВА 42", 0),
            entry("ГЛАВА 43", 0),
            entry("ГЛАВА 44", 0),
            entry("ГЛАВА 45", 0),
            entry("ГЛАВА 46", 0),
            entry("ГЛАВА 47", 0),
            entry("ГЛАВА 48", 0),
            entry("ГЛАВА 49", 0),
            entry("ГЛАВА 49/1", 0),
            entry("ГЛАВА 49/2", 0),
            entry("ГЛАВА 50", 0),
            entry("ГЛАВА 51", 0),
            entry("ГЛАВА 52", 0),
            entry("ГЛАВА 53", 0),
            entry("ГЛАВА 54", 0),
            entry("ГЛАВА 55", 0),
            entry("ГЛАВА 56", 0),
            entry("ГЛАВА 57", 0),
            entry("ГЛАВА 58", 0)
    );


    public static void validateUPK(UPK upk) {
        assertEquals(3, upk.getCodexParts().size());
        for (CodexPart codexPart : upk.getCodexParts()) {
            log.info("{}", codexPart.getFullNumber());

            var expectedSize = expectedCodexSizes.get(codexPart.getFullNumber().toUpperCase());
            assertEquals(expectedSize, codexPart.getSections().size());
            validateCodexPart(codexPart);
        }
    }

    private static void validateCodexPart(CodexPart codexPart) {
        for (Section section : codexPart.getSections()) {
            log.info("  {} - {}", section.getFullNumber(), section.getTitle());

            var expectedSize = expectedSectionSizes.get(section.getFullNumber().toUpperCase());
            assertEquals(expectedSize, section.getChapters().size());
            validateSection(section);
        }
    }

    private static void validateSection(Section section) {
        for (Chapter chapter : section.getChapters()) {
            log.info("    {} - {}", chapter.getFullNumber(), chapter.getTitle());

            var expectedSize = expectedChapterSizes.get(chapter.getNumberId().toUpperCase());
            // assertEquals(expectedSize, chapter.getArticles().size());
            validateChapters(chapter);
        }
    }

    private static void validateChapters(Chapter chapter) {
        for (Article article : chapter.getArticles()) {
            log.info("      {} - {}", article.getFullNumber(), article.getTitle());

            // var expectedSize = expectedChapterSizes.get(chapter.getNumber().toUpperCase());
            // assertEquals(expectedSize, chapter.getArticles().size());
            validateArticlesSizes(article);
        }
    }

    private static void validateArticlesSizes(Article article) {
        if (!article.getIntro().isBlank()) {
            log.info("        {}", article.getIntro());
        }
    }

}
