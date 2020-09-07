package com.etalon.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@ToString(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class CodexNode {

    @ToString.Include
    String id;

    @ToString.Include
    String title;

    String text;
    String link;
    List<String> notes;
    List<CodexNode> children;

    public String getFullNumber() {
        return getType().name() + " " + id;
    }

    public abstract CodexNodeType getType();

}
