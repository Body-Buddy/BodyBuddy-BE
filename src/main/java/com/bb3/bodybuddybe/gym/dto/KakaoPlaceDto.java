package com.bb3.bodybuddybe.gym.dto;

import com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto.Document;
import lombok.Getter;

@Getter
public class KakaoPlaceDto {
    private String placeId;
    private String placeName;
    private String placeUrl;
    private String roadAddress;

    public KakaoPlaceDto(Document document) {
        this.placeId = document.getId();
        this.placeName = document.getPlaceName();
        this.placeUrl = document.getPlaceUrl();
        this.roadAddress = document.getRoadAddressName();
    }
}
