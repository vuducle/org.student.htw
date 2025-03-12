package org.htw.student.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BIAS {
    NAYEON,
    JEONGYEON,
    MOMO,
    SANA,
    JIHYO,
    MINA,
    DAYHUN,
    CHAEYOUNG,
    TZUYU;

    @JsonCreator
    public static BIAS fromString(String value) {
        return BIAS.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return this.name();
    }
}
