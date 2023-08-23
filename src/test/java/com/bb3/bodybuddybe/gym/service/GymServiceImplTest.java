package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.dto.*;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.bb3.bodybuddybe.gym.dto.KakaoApiResponseDto.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymServiceImplTest {

    GymServiceImpl gymService;

    @Mock
    WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    GymRepository gymRepository;

    @Mock
    UserGymRepository userGymRepository;

    @Mock
    WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        gymService = new GymServiceImpl(gymRepository, userGymRepository, webClientBuilder);
    }

    @Test
    @DisplayName("헬스장을 검색하면 해당하는 헬스장의 목록을 반환한다.")
    void testSearchGyms() {
        // given
        String query = "건강";
        LocationDto location = new LocationDto();

        Document doc1 = new Document("스포애니 노원점", "스포츠,레저 > 스포츠시설 > 헬스클럽", "448766559");
        Document doc2 = new Document("J요가", "스포츠,레저 > 요가,필라테스 > 요가원", "27479746");
        Document doc3 = new Document("현이네 건강식", "음식점 > 한식", "1824317673");

        KakaoApiResponseDto mockResponse = new KakaoApiResponseDto(new Meta(), List.of(doc1, doc2, doc3));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq("Authorization"), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(KakaoApiResponseDto.class)).thenReturn(Mono.just(mockResponse));

        // when
        List<PlaceDto> result = gymService.searchGyms(query, location);

        // then
        assertEquals(2, result.size());

        PlaceDto place1 = result.get(0);
        assertEquals("448766559", place1.getId());
        assertEquals("스포애니 노원점", place1.getName());

        PlaceDto place2 = result.get(1);
        assertEquals("27479746", place2.getId());
        assertEquals("J요가", place2.getName());
    }

    @Test
    @DisplayName("DB에 존재하는 헬스장을 등록한다.")
    void testAddToMyGyms_addExistingGym() {
        // given
        GymRequestDto requestDto = new GymRequestDto();
        requestDto.setId("448766559");
        requestDto.setName("스포애니 노원점");

        User user = new User();
        Gym existingGym = new Gym(requestDto);

        when(gymRepository.findByKakaoPlaceId(requestDto.getId())).thenReturn(Optional.of(existingGym));

        // when
        gymService.addToMyGyms(requestDto, user);

        // then
        ArgumentCaptor<UserGym> userGymCaptor = ArgumentCaptor.forClass(UserGym.class);

        verify(gymRepository).findByKakaoPlaceId(requestDto.getId());
        verify(gymRepository, never()).save(any(Gym.class));
        verify(userGymRepository).save(userGymCaptor.capture());

        UserGym savedUserGym = userGymCaptor.getValue();
        assertEquals(requestDto.getId(), savedUserGym.getGym().getKakaoPlaceId());
        assertEquals(requestDto.getName(), savedUserGym.getGym().getName());
    }

    @Test
    @DisplayName("DB에 존재하지 않는 헬스장을 등록한다.")
    void testAddToMyGyms_addNewGym() {
        // given
        GymRequestDto requestDto = new GymRequestDto();
        requestDto.setId("27440935");
        requestDto.setName("에이블짐 노원본점");

        User user = new User();
        Gym newGym = new Gym(requestDto);

        when(gymRepository.findByKakaoPlaceId(requestDto.getId())).thenReturn(Optional.empty());
        when(gymRepository.save(any(Gym.class))).thenReturn(newGym);

        // when
        gymService.addToMyGyms(requestDto, user);

        // then
        ArgumentCaptor<Gym> gymCaptor = ArgumentCaptor.forClass(Gym.class);

        verify(gymRepository).findByKakaoPlaceId(requestDto.getId());
        verify(gymRepository).save(gymCaptor.capture());
        verify(userGymRepository).save(any(UserGym.class));

        Gym savedGym = gymCaptor.getValue();
        assertEquals(requestDto.getId(), savedGym.getKakaoPlaceId());
        assertEquals(requestDto.getName(), savedGym.getName());
    }

    @Test
    @DisplayName("중복된 헬스장을 등록하려고 할 때 예외를 발생시킨다.")
    void testAddToMyGyms_duplicateGym() {
        // given
        GymRequestDto requestDto = new GymRequestDto();
        requestDto.setId("448766559");
        requestDto.setName("스포애니 노원점");

        User user = new User();
        Gym existingGym = new Gym(requestDto);
        UserGym userGym = new UserGym(user, existingGym);

        when(gymRepository.findByKakaoPlaceId(requestDto.getId())).thenReturn(Optional.of(existingGym));
        when(userGymRepository.existsByUserAndGym(user, existingGym)).thenReturn(true);

        // when
        CustomException thrownException = assertThrows(CustomException.class, () -> {
            gymService.addToMyGyms(requestDto, user);
        });

        // then
        verify(gymRepository).findByKakaoPlaceId(requestDto.getId());
        verify(userGymRepository).existsByUserAndGym(user, existingGym);
        assertEquals(ErrorCode.DUPLICATED_MY_GYM, thrownException.getErrorCode());
    }

    @Test
    @DisplayName("나의 헬스장 목록을 조회한다.")
    void testGetMyGyms() {
        // given
        User user = new User();
        Gym gym1 = new Gym("448766559", "스포애니 노원점", "서울 노원구 동일로 1361");
        Gym gym2 = new Gym("27440935", "에이블짐 노원본점", "서울 노원구 상계로 77");
        List<UserGym> userGyms = List.of(new UserGym(user, gym1), new UserGym(user, gym2));

        when(userGymRepository.findAllByUser(user)).thenReturn(userGyms);

        // when
        List<GymResponseDto> result = gymService.getMyGyms(user);

        // then
        verify(userGymRepository).findAllByUser(user);
        assertEquals(2, result.size());

        GymResponseDto gymResponse1 = result.get(0);
        assertEquals("448766559", gymResponse1.getKakaoPlaceId());
        assertEquals("스포애니 노원점", gymResponse1.getName());
        assertEquals("서울 노원구 동일로 1361", gymResponse1.getRoadAddress());

        GymResponseDto gymResponse2 = result.get(1);
        assertEquals("27440935", gymResponse2.getKakaoPlaceId());
        assertEquals("에이블짐 노원본점", gymResponse2.getName());
        assertEquals("서울 노원구 상계로 77", gymResponse2.getRoadAddress());
    }

    @Test
    @DisplayName("나의 헬스장 목록에서 헬스장을 성공적으로 삭제한다.")
    void testDeleteFromMyGyms_success() {
        // given
        User user = new User();
        Long gymId = 1L;
        Gym gym = new Gym("448766559", "스포애니 노원점", "서울 노원구 동일로 1361");
        UserGym userGym = new UserGym(user, gym);

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(userGymRepository.findByUserAndGym(user, gym)).thenReturn(Optional.of(userGym));

        // when
        gymService.deleteFromMyGyms(gymId, user);

        // then
        verify(gymRepository).findById(gymId);
        verify(userGymRepository).findByUserAndGym(user, gym);
        verify(userGymRepository).delete(userGym);
    }

    @Test
    @DisplayName("등록하지 않은 헬스장을 삭제하려고 할 때 예외를 발생시킨다.")
    void testDeleteFromMyGyms_failure() {
        // given
        User user = new User();
        Long gymId = 1L;
        Gym gym = new Gym("27479746", "J요가", "서울 노원구 석계로 105");

        when(gymRepository.findById(gymId)).thenReturn(Optional.of(gym));
        when(userGymRepository.findByUserAndGym(user, gym)).thenReturn(Optional.empty());

        // when
        CustomException thrownException = assertThrows(CustomException.class, () -> {
            gymService.deleteFromMyGyms(gymId, user);
        });

        // then
        verify(gymRepository).findById(gymId);
        verify(userGymRepository).findByUserAndGym(user, gym);
        assertEquals(ErrorCode.NOT_MY_GYM, thrownException.getErrorCode());
    }
}