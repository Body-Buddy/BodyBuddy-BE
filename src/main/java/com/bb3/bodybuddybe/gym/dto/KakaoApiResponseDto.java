package com.bb3.bodybuddybe.gym.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoApiResponseDto {

    private Meta meta;
    private List<Document> documents;

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Meta {
        private SameName sameName;
        private int pageableCount;
        private int totalCount;
        private boolean isEnd;

        @Getter
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class SameName {
            private List<String> region;
            private String keyword;
            private String selectedRegion;
        }
    }

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Document {
        private String placeName;
        private String distance;
        private String placeUrl;
        private String categoryName;
        private String addressName;
        private String roadAddressName;
        private String id;
        private String phone;
        private String categoryGroupCode;
        private String categoryGroupName;
        private String x;
        private String y;

        public Document(String placeName, String categoryName, String id) {
            this.placeName = placeName;
            this.categoryName = categoryName;
            this.id = id;
        }
    }
}

