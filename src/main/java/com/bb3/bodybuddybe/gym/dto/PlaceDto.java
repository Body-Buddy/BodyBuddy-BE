package com.bb3.bodybuddybe.gym.dto;

import com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto.Document;
import lombok.Getter;

@Getter
public class PlaceDto {
    private String id;
    private String name;
    private String url;
    private String roadAddress;

    public PlaceDto(Document document) {
        this.id = document.getId();
        this.name = document.getPlaceName();
        this.url = document.getPlaceUrl();
        this.roadAddress = document.getRoadAddressName();
    }
}
