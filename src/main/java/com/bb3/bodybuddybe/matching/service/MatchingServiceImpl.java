package com.bb3.bodybuddybe.matching.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaResponseDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.repository.MatchingCriteriaRepository;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final MatchingCriteriaRepository matchingCriteriaRepository;

    @Override
    @Transactional
    public void createMatchingCriteria(CriteriaCreateRequestDto requestDto, User user) {
        MatchingCriteria matchingCriteria = new MatchingCriteria(user, requestDto);
        matchingCriteriaRepository.save(matchingCriteria);
    }

    @Override
    @Transactional(readOnly = true)
    public CriteriaResponseDto getMatchingCriteria(User user) {
        MatchingCriteria matchingCriteria = findByUser(user);
        return new CriteriaResponseDto(matchingCriteria);
    }

    @Override
    public void updateMatchingCriteria(CriteriaUpdateRequestDto requestDto, User user) {
        MatchingCriteria matchingCriteria = findByUser(user);
        matchingCriteria.update(requestDto);
    }

    private MatchingCriteria findByUser(User user) {
        return matchingCriteriaRepository.findByUser(user).orElseThrow(() ->
                new CustomException(ErrorCode.MATCHING_CRITERIA_NOT_FOUND));
    }
}
