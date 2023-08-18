package com.bb3.bodybuddybe.gym.controller;

import com.bb3.bodybuddybe.gym.dto.KakaoPlaceDto;
import com.bb3.bodybuddybe.gym.dto.LocationDto;
import com.bb3.bodybuddybe.gym.service.GymServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GymController {

    private final GymServiceImpl gymService;

    @GetMapping("/gyms/search")
    public ResponseEntity<List<KakaoPlaceDto>> searchGyms(@RequestParam String query,
                                                          @ModelAttribute LocationDto location) {
        List<KakaoPlaceDto> gyms = gymService.searchGyms(query, location);
        return ResponseEntity.ok(gyms);
    }

//    @PostMapping("/users/{userId}/gyms")
//    public ResponseEntity<ApiResponseDto> addToMyGym(@RequestBody GymAddRequestDto requestDto,
//                                                   @PathVariable Long userId,
//                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        gymService.addToMyGym(requestDto, userId, userDetails.getUser());
//        return ResponseEntity.ok(new ApiResponseDto(HttpStatus.OK.value(), "나의 헬스장 등록 성공"));
//    }
}
