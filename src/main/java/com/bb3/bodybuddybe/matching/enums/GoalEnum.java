package com.bb3.bodybuddybe.matching.enums;

public enum GoalEnum {
    FAT_REDUCTION("체지방 줄이기"),
    MUSCLE_GAIN("근육량 늘리기"),
    STAMINA_IMPROVEMENT("체력 키우기"),
    BODY_PROFILE("바디 프로필"),
    MAINTAIN_BODY("몸 유지하기"),
    BUILD_HEALTHY_HABIT("건강한 습관 만들기");

    private final String description;

    GoalEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

