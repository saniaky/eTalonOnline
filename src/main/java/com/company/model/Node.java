package com.company.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Node {

    String numberId;
    String title;

    public String getFullNumber() {
        return getType() + " " + numberId;
    }

    public abstract String getType();

}
