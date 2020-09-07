package com.etalon.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Set;

import static com.etalon.utils.ParsingUtils.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Set.of;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("SpellCheckingInspection")
public enum CodexNodeType {

    CODEX_NAME(of("titlek")), // UNIQUE
    CODEX_DATE(of("datepr")), // .newncpi .datepr, UNIQUE
    CODEX_NUMBER(of("number")), // .newncpi .number UNIQUE
    CODEX_ACCEPTED(of("prinodobren")), // UNIQUE

    CHANGES_TITLE(of("changei")), // Правки в кодексе, UNIQUE
    CHANGE(of("changeadd")), // Пункт правки

    CONTENT_TITLE(of("contentword")), // Оглавление, UNIQUE
    CONTENT_LINE(of("contenttext")), // Пункт в оглавлении

    CODEX_PART(of("part")), // Часть Кодекса
    SECTION(of("zagrazdel")), // Раздел
    CHAPTER(of("chapter")), // Глава
    ARTICLE(of("article")), // Статья
    ARTICLE_PART(of("")),
    ARTICLE_PARAGRAPH(of("")),
    ARTICLE_COMMENT(of("comment")), // Примечание к статье
    POINT(of("point", "underpoint")), // Пункт статьи

    TEXT(of("newncpi")), // текст после .article or .chapter

    NOTE_LINE(of("snoskiline")),
    NOTE(of("snoski"));

    private final Set<String> classNames;

    public String getCSSSelector() {
        return "." + String.join(" , .", this.getClassNames());
    }

    public boolean isFit(Element element) {
        if (isEnd(element) || isBlank(element)) {
            return false;
        }
        switch (this) {
            case CHANGE:
                return isCodexChange(element);
            case CODEX_PART:
                return isCodexPart(element);
            case SECTION:
                return isSection(element);
            case CHAPTER:
                return isChapter(element);
            case ARTICLE:
                return isArticle(element);
        }
        return false;
    }

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
                return asList(TEXT);
            case ARTICLE:
                return asList();
        }
        return emptyList();
    }

    public boolean isGoingUp(Element element) {
        if (isEnd(element)) {
            return true;
        }
        switch (this) {
            case CHANGE:
                return isContentTitle(element);
            case CODEX_PART:
                return isEnd(element);
            case SECTION:
                return isCodexPart(element);
            case CHAPTER:
                return isCodexPart(element) || isSection(element);
            case ARTICLE:
                return isCodexPart(element) || isSection(element) || isChapter(element);
        }
        return true;
    }
}
