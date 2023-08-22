package com.bb3.bodybuddybe.matching.controller;

import com.bb3.bodybuddybe.common.dto.ApiResponseDto;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaResponseDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.matching.service.MatchingServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/criteria")
public class MatchingController {

    private final MatchingServiceImpl matchingService;

    @PostMapping
    ResponseEntity<ApiResponseDto> createMatchingCriteria(@RequestBody @Valid CriteriaCreateRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        matchingService.createMatchingCriteria(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("매칭 기준 생성 성공", HttpStatus.OK.value()));
    }

    @GetMapping
    ResponseEntity<CriteriaResponseDto> getMatchingCriteria(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CriteriaResponseDto criteria = matchingService.getMatchingCriteria(userDetails.getUser());
        return ResponseEntity.ok(criteria);
    }

    @PutMapping
    ResponseEntity<ApiResponseDto> updateMatchingCriteria(@RequestBody @Valid CriteriaUpdateRequestDto requestDto,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        matchingService.updateMatchingCriteria(requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponseDto("매칭 기준 수정 성공", HttpStatus.OK.value()));
    }
}
