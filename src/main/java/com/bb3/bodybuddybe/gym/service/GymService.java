package com.bb3.bodybuddybe.gym.service;

import com.bb3.bodybuddybe.gym.dto.PlaceDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;

import java.util.List;

public interface GymService {
    List<PlaceDto> searchGyms(String query, LocationDto location);
}
