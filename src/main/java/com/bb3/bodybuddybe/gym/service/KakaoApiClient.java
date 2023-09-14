package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoApiClient {

    private final WebClient webClient;

    @Value("${kakao.api.key}")
    private String restApiKey;
    private static final String KAKAO_API_PATH = "/v2/local/search/keyword.json";
    private static final String KAKAO_AUTHORIZATION_PREFIX = "KakaoAK ";

    public List<KakaoApiResponseDto.Document> fetchPlacesFromKakaoAPI(String query, LocationDto location) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(KAKAO_API_PATH)
                        .queryParam("query", query)
                        .queryParam("x", location.getX())
                        .queryParam("y", location.getY())
                        .build())
                .header("Authorization", KAKAO_AUTHORIZATION_PREFIX + restApiKey)
                .retrieve()
                .bodyToMono(KakaoApiResponseDto.class)
                .block()
                .getDocuments();
    }
}
