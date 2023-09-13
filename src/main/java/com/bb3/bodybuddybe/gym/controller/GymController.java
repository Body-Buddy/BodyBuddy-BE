package com.bb3.bodybuddybe.gym.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.gym.dto.GymRequestDto;
import com.bb3.bodybuddybe.gym.dto.GymResponseDto;
import com.bb3.bodybuddybe.gym.dto.PlaceDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import com.bb3.bodybuddybe.gym.service.GymServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GymController {

    private final GymServiceImpl gymService;

    @GetMapping("/gyms/search")
    public ResponseEntity<List<PlaceDto>> searchGyms(@RequestParam String query,
                                                     @ModelAttribute LocationDto location) {
        List<PlaceDto> gyms = gymService.searchGyms(query, location);
        return ResponseEntity.ok(gyms);
    }

    @PostMapping("/users/{userId}/gyms")
    public ResponseEntity<ApiResponseDto> addToMyGyms(@RequestBody GymRequestDto requestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        gymService.addToMyGyms(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("나의 헬스장 등록 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/users/{userId}/gyms")
    public ResponseEntity<List<GymResponseDto>> getMyGyms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<GymResponseDto> gyms = gymService.getMyGyms(userDetails.getUser());
        return ResponseEntity.ok(gyms);
    }

    @DeleteMapping("/users/{userId}/gyms/{gymId}")
    public ResponseEntity<ApiResponseDto> deleteFromMyGyms(@PathVariable Long gymId,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        gymService.deleteFromMyGyms(gymId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("나의 헬스장 삭제 성공", HttpStatus.OK.value()));
    }
}
