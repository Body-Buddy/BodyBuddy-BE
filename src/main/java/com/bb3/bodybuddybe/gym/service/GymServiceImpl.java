package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import com.bb3.bodybuddybe.gym.dto.PlaceDto;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto.Document;

@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private static final String KAKAO_API_PATH = "/v2/local/search/keyword.json";
    private static final String KAKAO_AUTHORIZATION_PREFIX = "KakaoAK ";
    private static final String SPORTS_CATEGORY = "스포츠,레저";

    private final WebClient webClient;
    private final GymRepository gymRepository;
    private final UserGymRepository userGymRepository;

    @Value("${kakao.api.key}")
    private String restApiKey;

    @Override
    public List<PlaceDto> searchGyms(String query, LocationDto location) {
        return getFilteredPlaces(query, location)
                .stream()
                .map(PlaceDto::new)
                .toList();
    }

    private List<Document> getFilteredPlaces(String query, LocationDto location) {
        return fetchPlacesFromKakaoAPI(query, location)
                .stream()
                .filter(this::isSportsCategory)
                .toList();
    }

    private boolean isSportsCategory(Document document) {
        return document.getCategoryName().contains(SPORTS_CATEGORY);
    }

    private List<Document> fetchPlacesFromKakaoAPI(String query, LocationDto location) {
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
