package com.company;

import com.company.model.Chapter;
import com.company.model.CodexPart;
import com.company.model.Section;
import com.company.model.UPK;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SizeTest {

    static Map<String, Integer> expectedCodexSizes = Map.of(
            "ЧАСТЬ ПЕРВАЯ", 6,
            "ЧАСТЬ ВТОРАЯ", 2,
            "ЧАСТЬ ТРЕТЬЯ", 8
    );

    static Map<String, Integer> expectedSectionSizes = Map.ofEntries(
            // Section 1
            entry("РАЗДЕЛ I", 3),
            entry("РАЗДЕЛ II", 6),
            entry("РАЗДЕЛ III", 2),
            entry("РАЗДЕЛ IV", 3),
            entry("РАЗДЕЛ V", 2),
            entry("РАЗДЕЛ VI", 3),
            // Section 2
            entry("РАЗДЕЛ VII", 2),
            entry("РАЗДЕЛ VIII", 10),
            // Section 3
            entry("РАЗДЕЛ IX", 7),
            entry("РАЗДЕЛ X", 2),
            entry("РАЗДЕЛ XI", 1),
            entry("РАЗДЕЛ XII", 1),
            entry("РАЗДЕЛ XIII", 1),
            entry("РАЗДЕЛ XIV", 8),
            entry("РАЗДЕЛ XV", 8),
            entry("РАЗДЕЛ XVI", 1)
    );


    static Map<String, Integer> expectedChapterSizes = Map.ofEntries(
            entry("ГЛАВА 1", 0),
            entry("ГЛАВА 2", 0),
            entry("ГЛАВА 3", 0),
            entry("ГЛАВА 4", 0),
            entry("ГЛАВА 5", 0),
            entry("ГЛАВА 6", 0),
            entry("ГЛАВА 7", 0),
            entry("ГЛАВА 8", 0),
            entry("ГЛАВА 9", 0),
            entry("ГЛАВА 10", 0),
            entry("ГЛАВА 11", 0),
            entry("ГЛАВА 12", 0),
            entry("ГЛАВА 13", 0),
            entry("ГЛАВА 14", 0),
            entry("ГЛАВА 15", 0),
            entry("ГЛАВА 16", 0),
            entry("ГЛАВА 17", 0),
            entry("ГЛАВА 18", 0),
            entry("ГЛАВА 19", 0),
            entry("ГЛАВА 20", 0),
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


    public static void validateUPKSize(UPK upk) {
        assertEquals(3, upk.getCodexParts().size());
        validateCodex(upk);
    }

    private static void validateCodex(UPK upk) {
        for (CodexPart codexPart : upk.getCodexParts()) {
            log.info("{}", codexPart.getNumber());
            codexPart.getSections().forEach(s -> log.info("  {} - {}", s.getNumber(), s.getName()));

            var expectedSize = expectedCodexSizes.get(codexPart.getNumber().toUpperCase());
            assertEquals(expectedSize, codexPart.getSections().size());
            validateSectionSizes(codexPart);
        }
    }

    private static void validateSectionSizes(CodexPart codexPart) {
        for (Section section : codexPart.getSections()) {
            section.getChapters().forEach(s -> log.info("    {} - {}", s.getNumber(), s.getName()));

            var expectedSize = expectedSectionSizes.get(section.getNumber().toUpperCase());
            assertEquals(expectedSize, section.getChapters().size());
            validateChapterSizes(section);
        }
    }

    private static void validateChapterSizes(Section section) {
        for (Chapter chapter : section.getChapters()) {
            chapter.getArticles().forEach(s -> log.info("      {} - {}", s.getNumber(), s.getName()));

            var expectedSize = expectedChapterSizes.get(chapter.getNumber().toUpperCase());
            assertEquals(expectedSize, chapter.getArticles().size());
            // validateArticlesSizes(section);
        }
    }

}
