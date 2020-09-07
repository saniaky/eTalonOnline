package com.etalon.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public abstract class CodexNode {

    String id;
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
