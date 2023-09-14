package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.dto.GymRequestDto;
import com.bb3.bodybuddybe.gym.dto.GymResponseDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import com.bb3.bodybuddybe.gym.dto.PlaceDto;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto.Document;

@Service
@RequiredArgsConstructor
public class GymServiceImpl implements GymService {

    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final GymRepository gymRepository;
    private final UserGymRepository userGymRepository;
    private static final String SPORTS_CATEGORY = "스포츠,레저";

    @Override
    public List<PlaceDto> searchGyms(String query, LocationDto location) {
        return getFilteredPlaces(query, location)
                .stream()
                .map(PlaceDto::new)
                .toList();
    }

    private List<Document> getFilteredPlaces(String query, LocationDto location) {
        return kakaoApiClient.fetchPlacesFromKakaoAPI(query, location)
                .stream()
                .filter(this::isSportsCategory)
                .toList();
    }

    private boolean isSportsCategory(Document document) {
        return document.getCategoryName().contains(SPORTS_CATEGORY);
    }

    @Override
    @Transactional
    public void addToMyGyms(GymRequestDto requestDto, User user) {
        Gym gym = gymRepository.findByKakaoPlaceId(requestDto.getId())
                .orElseGet(() -> gymRepository.save(new Gym(requestDto)));

        if (userGymRepository.existsByUserAndGym(user, gym)) {
            throw new CustomException(ErrorCode.DUPLICATED_MY_GYM);
        }
        user.markedAsRegisteredGym();
        userRepository.save(user);
        userGymRepository.save(new UserGym(user, gym));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymResponseDto> getMyGyms(User user) {
        return userGymRepository.findAllByUser(user)
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
