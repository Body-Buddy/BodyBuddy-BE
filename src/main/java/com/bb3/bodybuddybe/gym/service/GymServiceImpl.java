package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.dto.*;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto.Document;

@Service
public class GymServiceImpl implements GymService {

    private static final String KAKAO_API_PATH = "/v2/local/search/keyword.json";
    private static final String KAKAO_AUTHORIZATION_PREFIX = "KakaoAK ";
    private static final String SPORTS_CATEGORY = "스포츠,레저";

    private final WebClient webClient;
    private final GymRepository gymRepository;
    private final UserGymRepository userGymRepository;

    @Value("${kakao.api.key}")
    private String restApiKey;

    public GymServiceImpl(GymRepository gymRepository, UserGymRepository userGymRepository, WebClient.Builder webClientBuilder) {
        this.gymRepository = gymRepository;
        this.userGymRepository = userGymRepository;
        this.webClient = webClientBuilder.baseUrl("https://dapi.kakao.com").build();
    }

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

    @Override
    @Transactional
    public void addToMyGyms(GymRequestDto requestDto, User user) {
        Gym gym = gymRepository.findByKakaoPlaceId(requestDto.getId())
                .orElseGet(() -> gymRepository.save(new Gym(requestDto)));
        userGymRepository.save(new UserGym(user, gym));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymResponseDto> getMyGyms(User user) {
        return userGymRepository.findByUser(user)
                .stream()
                .map(UserGym::getGym)
                .map(GymResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void deleteFromMyGyms(Long gymId, User user) {
        Gym gym = findGym(gymId);
        UserGym userGym = findUserGym(user, gym);
        userGymRepository.delete(userGym);
    }

    private Gym findGym(Long gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new CustomException(ErrorCode.GYM_NOT_FOUND));
    }

    private UserGym findUserGym(User user, Gym gym) {
        return userGymRepository.findByUserAndGym(user, gym)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_MY_GYM));
    }
}
