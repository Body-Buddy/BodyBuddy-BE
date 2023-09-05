package com.bb3.bodybuddybe.post.enums;

public enum CategoryEnum {
    FIND_FRIENDS("친구 찾기"),
    MEMBER_INTRODUCTIONS("회원 소개"),
    WORKOUT_TIPS("운동 팁"),
    SHARE_DIET("식단 공유"),
    EQUIPMENT_REVIEWS("기구 리뷰"),
    BUY_AND_SELL("중고 장터"),
    QNA("Q&A"),
    ETC("기타");

    private final String description;

    CategoryEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
