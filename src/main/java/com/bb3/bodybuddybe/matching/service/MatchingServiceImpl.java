package com.bb3.bodybuddybe.matching.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaResponseDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.GoalEnum;
import com.bb3.bodybuddybe.matching.repository.MatchingCriteriaRepository;
import com.bb3.bodybuddybe.user.dto.UserProfileDto;
import com.bb3.bodybuddybe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final UserGymRepository userGymRepository;
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
    @Transactional
    public void updateMatchingCriteria(CriteriaUpdateRequestDto requestDto, User user) {
        MatchingCriteria matchingCriteria = findByUser(user);
        matchingCriteria.update(requestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> getMatchingUsers(Long gymId, User user) {
        if (!userGymRepository.existsByUserAndGymId(user, gymId)) {
            throw new CustomException(ErrorCode.NOT_MY_GYM);
        }

        List<User> allUsersInGym = userGymRepository.findAllByGymId(gymId)
                .stream()
                .map(UserGym::getUser)
                .toList();

        MatchingCriteria userCriteria = findByUser(user);

        return allUsersInGym.stream()
                .filter(other -> !other.getId().equals(user.getId()))
                .sorted(Comparator.comparingInt(other -> -calculateMatchScore(userCriteria, other)))
                .map(UserProfileDto::new)
                .toList();
    }

    private int calculateMatchScore(MatchingCriteria userCriteria, User other) {
        User user = userCriteria.getUser();
        MatchingCriteria otherCriteria = findByUser(other);

        int score = 0;
        if (userCriteria.getPreferSameGender() && user.getGender() == other.getGender()) {
            score += 20;
        }

        if (userCriteria.getPreferSameAgeRange() && user.getAgeRange() == other.getAgeRange()) {
            score += 10;
        }

        if (userCriteria.getExperience() == otherCriteria.getExperience()) {
            score += 5;
        }

        if (userCriteria.getIntensity() == otherCriteria.getIntensity()) {
            score += 5;
        }

        if (userCriteria.getExerciseTime() == otherCriteria.getExerciseTime()) {
            score += 5;
        }

        Set<GoalEnum> userGoals = userCriteria.getGoals();
        Set<GoalEnum> otherGoals = otherCriteria.getGoals();
        for (GoalEnum goal : userGoals) {
            if (otherGoals.contains(goal)) {
                score += 3;
            }
        }

        return score;
    }

    private MatchingCriteria findByUser(User user) {
        return matchingCriteriaRepository.findByUser(user).orElseThrow(() ->
                new CustomException(ErrorCode.MATCHING_CRITERIA_NOT_FOUND));
    }
}
