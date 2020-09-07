package com.etalon.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Getter
@RequiredArgsConstructor
public enum CodexNodeType {

    CODEX_NAME, // UNIQUE
    CODEX_DATE, // UNIQUE
    CODEX_NUMBER, // UNIQUE
    CODEX_ACCEPTED, // UNIQUE

    CHANGE_ENTRY, // Пункт правки
    CONTENT_ENTRY, // Пункт в оглавлении

    CODEX_PART, // Часть Кодекса
    SECTION, // Раздел
    CHAPTER, // Глава
    ARTICLE, // Статья
    ARTICLE_PART, // Часть статьи
    ARTICLE_PARAGRAPH, // Пункт статьи
    ARTICLE_COMMENT, // Примечание к статье

    TEXT,
    NOTE;

    /**
     * @return types of elements that can be found on the first level deep for this element
     */
    public List<CodexNodeType> getFirstDeepChildren() {
        switch (this) {
            case CODEX_PART:
                return asList(TEXT, SECTION);
            case SECTION:
                return asList(TEXT, CHAPTER);
            case CHAPTER:
                return asList(TEXT, ARTICLE);
            case ARTICLE:
                return asList(TEXT, ARTICLE_PART, ARTICLE_PARAGRAPH, ARTICLE_COMMENT);
            case ARTICLE_PART:
                return asList(TEXT, ARTICLE_PARAGRAPH);
            default:
                return emptyList();
        }
    }

}
