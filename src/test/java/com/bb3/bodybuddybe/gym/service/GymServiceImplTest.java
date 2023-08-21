package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.GymRequestDto;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.gym.repository.GymRepository;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymServiceImplTest {

    @InjectMocks
    GymServiceImpl gymService;

    @Mock
    GymRepository gymRepository;

    @Mock
    UserGymRepository userGymRepository;

    @Test
    @DisplayName("DB에 존재하는 헬스장을 등록하는 경우")
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
    @DisplayName("DB에 존재하지 않는 헬스장을 등록하는 경우")
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
        assertEquals(0, savedGym.getMembers().size());
    }
}