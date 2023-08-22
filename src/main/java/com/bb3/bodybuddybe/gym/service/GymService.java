package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.GymRequestDto;
import com.bb3.bodybuddybe.gym.dto.GymResponseDto;
import com.bb3.bodybuddybe.gym.dto.PlaceDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GymService {
    List<PlaceDto> searchGyms(String query, LocationDto location);

    @Transactional
    void addToMyGyms(GymRequestDto requestDto, User user);

    @Transactional(readOnly = true)
    List<GymResponseDto> getMyGyms(User user);

    @Transactional
    void deleteFromMyGyms(Long gymId, User user);
}
