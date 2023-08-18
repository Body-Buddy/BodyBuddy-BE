package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto;
import com.bb3.bodybuddybe.gym.dto.KakaoPlaceDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private static final String KAKAO_API_PATH = "/v2/local/search/keyword.json";
    private static final String AUTHORIZATION_PREFIX = "KakaoAK ";
    private static final String SPORTS_CATEGORY = "스포츠,레저";

    private final WebClient webClient;
    private final GymRepository gymRepository;
    private final UserGymRepository userGymRepository;

    @Value("${kakao-api-key}")
    String restApiKey;

    public List<KakaoPlaceDto> searchGyms(String query, LocationDto location) {
        KakaoApiResponseDto response = getKakaoPlaces(query, location);

        return response.getDocuments()
                .stream()
                .filter(document -> document.getCategoryName().contains(SPORTS_CATEGORY))
                .map(KakaoPlaceDto::new)
                .toList();
    }

    private KakaoApiResponseDto getKakaoPlaces(String query, LocationDto location) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(KAKAO_API_PATH)
                        .queryParam("query", query)
                        .queryParam("x", location.getX())
                        .queryParam("y", location.getY())
                        .build())
                .header("Authorization", AUTHORIZATION_PREFIX + restApiKey)
                .retrieve()
                .bodyToMono(KakaoApiResponseDto.class)
                .block();
    }
}
