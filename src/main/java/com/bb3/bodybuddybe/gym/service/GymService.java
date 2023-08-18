package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.KakaoPlaceDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;

import java.util.List;

public interface GymService {
    List<KakaoPlaceDto> searchGyms(String query, LocationDto location);
}
