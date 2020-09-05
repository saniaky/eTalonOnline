package com.company.model;

import lombok.Builder;
import lombok.Data;

/**
 * Изменения и дополнения
 */
@Data
@Builder
public class CodexChange {

    String link;
    String number;
    String name;

}
