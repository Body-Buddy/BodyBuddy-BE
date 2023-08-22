package com.bb3.bodybuddybe.matching.service;

import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaResponseDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface MatchingService {
    @Transactional
    void createMatchingCriteria(CriteriaCreateRequestDto requestDto, User user);

    @Transactional(readOnly = true)
    CriteriaResponseDto getMatchingCriteria(User user);

    void updateMatchingCriteria(CriteriaUpdateRequestDto requestDto, User user);
}
