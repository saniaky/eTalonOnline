package com.etalon.html;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
enum CSSClassName {

    CODEX_NAME("titlek"), // UNIQUE
    CODEX_DATE("datepr"), // .newncpi .datepr, UNIQUE
    CODEX_NUMBER("number"), // .newncpi .number UNIQUE
    CODEX_ACCEPTED("prinodobren"), // UNIQUE

    CHANGE_TITLE("changei"), // Правки в кодексе, UNIQUE
    CHANGE_TEXT("changeadd"), // Пункт правки

    CONTENT_TITLE("contentword"), // Оглавление, UNIQUE
    CONTENT_TEXT("contenttext"), // Пункт в оглавлении

    CODEX_PART("part"), // Часть Кодекса
    SECTION("zagrazdel"), // Раздел
    CHAPTER("chapter"), // Глава
    ARTICLE("article"), // Статья
    POINT("point"), // Пункт статьи
    UNDERPOINT("underpoint"), // Пункт статьи
    ARTICLE_COMMENT("comment"), // Примечание к статье
    TEXT("newncpi"), // текст после .article or .chapter

    NOTE_LINE("snoskiline"),
    NOTE("snoski"),

    TITLEP("titlep"); // Заголовок приложения?

    @Getter
    private final String className;

}
