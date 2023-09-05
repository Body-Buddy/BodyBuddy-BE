package com.bb3.bodybuddybe.matching.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaResponseDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.matching.service.MatchingServiceImpl;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MatchingController {

    private final MatchingServiceImpl matchingService;

    @PostMapping("/users/{userId}/criteria")
    ResponseEntity<ApiResponseDto> createMatchingCriteria(@RequestBody @Valid CriteriaCreateRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        matchingService.createMatchingCriteria(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("매칭 기준 생성 성공", HttpStatus.OK.value()));
    }   //

    @GetMapping("/users/{userId}/criteria")
    ResponseEntity<CriteriaResponseDto> getMatchingCriteria(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CriteriaResponseDto criteria = matchingService.getMatchingCriteria(userDetails.getUser());
        return ResponseEntity.ok(criteria);
    }

    @PutMapping("/users/{userId}/criteria")
    ResponseEntity<ApiResponseDto> updateMatchingCriteria(@RequestBody @Valid CriteriaUpdateRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        matchingService.updateMatchingCriteria(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("매칭 기준 수정 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/gyms/{gymId}/matches")
    ResponseEntity<List<ProfileResponseDto>> getMatchingUsers(@PathVariable Long gymId,
                                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ProfileResponseDto> matchingUsers = matchingService.getMatchingUsers(gymId, userDetails.getUser());
        return ResponseEntity.ok(matchingUsers);
    }
}
