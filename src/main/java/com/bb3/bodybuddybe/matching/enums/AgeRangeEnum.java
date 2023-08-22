package com.bb3.bodybuddybe.matching.enums;

public enum AgeRangeEnum {
    S10s("10s"),
    S20s("20s"),
    S30s("30s"),
    S40s("40s"),
    S50s("50s"),
    S60plus("60+");

    private final String description;

    AgeRangeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

