package com.bb3.bodybuddybe.matching.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.gym.repository.UserGymRepository;
import com.bb3.bodybuddybe.matching.dto.CriteriaCreateRequestDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaResponseDto;
import com.bb3.bodybuddybe.matching.dto.CriteriaUpdateRequestDto;
import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.matching.enums.*;
import com.bb3.bodybuddybe.matching.repository.MatchingCriteriaRepository;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchingServiceImplTest {
    @InjectMocks
    MatchingServiceImpl matchingService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserGymRepository userGymRepository;

    @Mock
    MatchingCriteriaRepository matchingCriteriaRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자의 매칭 기준을 생성한다.")
    void testCreateMatchingCriteria() {
        // given
        User user = mock(User.class);
        CriteriaCreateRequestDto requestDto = CriteriaCreateRequestDto.builder()
                .preferSameGender(true)
                .preferSameAgeRange(true)
                .goals(Set.of(GoalEnum.FAT_REDUCTION, GoalEnum.BUILD_HEALTHY_HABIT))
                .experience(ExperienceEnum.INTERMEDIATE)
                .intensity(IntensityEnum.HIGH)
                .exerciseTime(ExerciseTimeEnum.NIGHT)
                .build();

        // when
        matchingService.createMatchingCriteria(requestDto, user);

        // then
        ArgumentCaptor<MatchingCriteria> criteriaCaptor = ArgumentCaptor.forClass(MatchingCriteria.class);

        verify(matchingCriteriaRepository).save(criteriaCaptor.capture());

        MatchingCriteria savedCriteria = criteriaCaptor.getValue();
        assertEquals(requestDto.getPreferSameGender(), savedCriteria.getPreferSameGender());
        assertEquals(requestDto.getGoals(), savedCriteria.getGoals());
        assertEquals(requestDto.getExerciseTime(), savedCriteria.getExerciseTime());
    }

    @Test
    @DisplayName("사용자의 매칭 기준을 조회한다.")
    void testGetMatchingCriteria() {
        // given
        User user = mock(User.class);
        MatchingCriteria criteria = mock(MatchingCriteria.class);

        when(criteria.getPreferSameAgeRange()).thenReturn(false);
        when(criteria.getExperience()).thenReturn(ExperienceEnum.BEGINNER);
        when(matchingCriteriaRepository.findByUser(user)).thenReturn(Optional.of(criteria));

        // when
        CriteriaResponseDto responseDto = matchingService.getMatchingCriteria(user);

        // then
        assertFalse(responseDto.getPreferSameAgeRange());
        assertEquals(ExperienceEnum.BEGINNER, responseDto.getExperience());
    }

    @Test
    @DisplayName("사용자의 매칭 기준을 수정한다.")
    void testUpdateMatchingCriteria() {
        // given
        User user = mock(User.class);
        CriteriaUpdateRequestDto requestDto = mock(CriteriaUpdateRequestDto.class);
        MatchingCriteria criteria = mock(MatchingCriteria.class);

        when(matchingCriteriaRepository.findByUser(user)).thenReturn(Optional.of(criteria));

        // when
        matchingService.updateMatchingCriteria(requestDto, user);

        // then
        verify(criteria).update(requestDto);
        verify(matchingCriteriaRepository).findByUser(user);
    }

    @Test
    @DisplayName("같은 헬스장의 유저들을 매칭 점수가 높은 순으로 조회한다.")
    void testGetMatchingUsers_success() {
        // given
        Long gymId = 1L;
        Gym gym = mock(Gym.class);

        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getGender()).thenReturn(GenderEnum.F);
        when(user.getAgeRange()).thenReturn(AgeRangeEnum.S20s);

        User other1 = mock(User.class);
        when(other1.getId()).thenReturn(2L);
        when(other1.getGender()).thenReturn(GenderEnum.F);
        when(other1.getAgeRange()).thenReturn(AgeRangeEnum.S30s);

        User other2 = mock(User.class);
        when(other2.getId()).thenReturn(3L);
        when(other2.getGender()).thenReturn(GenderEnum.M);
        when(other2.getAgeRange()).thenReturn(AgeRangeEnum.S20s);

        User other3 = mock(User.class);
        when(other3.getId()).thenReturn(4L);
        when(other3.getGender()).thenReturn(GenderEnum.M);
        when(other3.getAgeRange()).thenReturn(AgeRangeEnum.S50s);

        List<UserGym> allUsersInGym = List.of(
                new UserGym(user, gym),
                new UserGym(other1, gym),
                new UserGym(other2, gym),
                new UserGym(other3, gym)
        );

        when(userGymRepository.existsByUserAndGymId(user, gymId)).thenReturn(true);
        when(userGymRepository.findAllByGymId(gymId)).thenReturn(allUsersInGym);

        MatchingCriteria userCriteria = new MatchingCriteria(
                user, CriteriaCreateRequestDto.builder()
                .preferSameGender(true)
                .preferSameAgeRange(true)
                .goals(Set.of(GoalEnum.FAT_REDUCTION, GoalEnum.BUILD_HEALTHY_HABIT))
                .experience(ExperienceEnum.INTERMEDIATE)
                .intensity(IntensityEnum.HIGH)
                .exerciseTime(ExerciseTimeEnum.NIGHT)
                .build());

        MatchingCriteria other1Criteria = new MatchingCriteria(
                other1, CriteriaCreateRequestDto.builder()
                .preferSameGender(true) // +20
                .preferSameAgeRange(true)
                .goals(Set.of(GoalEnum.STAMINA_IMPROVEMENT))
                .experience(ExperienceEnum.INTERMEDIATE) // +5
                .intensity(IntensityEnum.HIGH) // +5
                .exerciseTime(ExerciseTimeEnum.EVENING)
                .build());

        MatchingCriteria other2Criteria = new MatchingCriteria(
                other2, CriteriaCreateRequestDto.builder()
                .preferSameGender(true)
                .preferSameAgeRange(true) // +10
                .goals(Set.of(GoalEnum.FAT_REDUCTION, GoalEnum.BUILD_HEALTHY_HABIT)) // +6
                .experience(ExperienceEnum.INTERMEDIATE) // +5
                .intensity(IntensityEnum.HIGH) // +5
                .exerciseTime(ExerciseTimeEnum.NIGHT) // +5
                .build());

        MatchingCriteria other3Criteria = new MatchingCriteria(
                other3, CriteriaCreateRequestDto.builder()
                .preferSameGender(true)
                .preferSameAgeRange(true)
                .goals(Set.of(GoalEnum.FAT_REDUCTION)) // +3
                .experience(ExperienceEnum.INTERMEDIATE) // +5
                .intensity(IntensityEnum.HIGH) // +5
                .exerciseTime(ExerciseTimeEnum.NIGHT) // +5
                .build());

        when(matchingCriteriaRepository.findByUser(user)).thenReturn(Optional.of(userCriteria));
        when(matchingCriteriaRepository.findByUser(other1)).thenReturn(Optional.of(other1Criteria));
        when(matchingCriteriaRepository.findByUser(other2)).thenReturn(Optional.of(other2Criteria));
        when(matchingCriteriaRepository.findByUser(other3)).thenReturn(Optional.of(other3Criteria));

        // when
        List<ProfileResponseDto> matchingUsers = matchingService.getMatchingUsers(gymId, user);

        // then
        verify(userGymRepository).findAllByGymId(gymId);
        assertEquals(3, matchingUsers.size());
        assertTrue(matchingUsers.stream().noneMatch(userProfile -> userProfile.getId().equals(user.getId())));
        assertEquals(other2.getId(), matchingUsers.get(0).getId()); // score: 31
        assertEquals(other1.getId(), matchingUsers.get(1).getId()); // score: 30
        assertEquals(other3.getId(), matchingUsers.get(2).getId()); // score: 18
    }

    @Test
    @DisplayName("사용자가 등록하지 않은 헬스장의 매칭 유저를 조회할 때 에러를 발생시킨다.")
    public void testGetMatchingUsers_failure() {
        // given
        Long gymId = 1L;
        User currentUser = mock(User.class);

        when(userGymRepository.existsByUserAndGymId(currentUser, gymId)).thenReturn(false);

        // when
        CustomException thrownException = assertThrows(CustomException.class, () ->
            matchingService.getMatchingUsers(gymId, currentUser)
        );

        // then
        verify(userGymRepository).existsByUserAndGymId(currentUser, gymId);
        verify(userGymRepository, never()).findAllByGymId(gymId);
        assertEquals(ErrorCode.NOT_MY_GYM, thrownException.getErrorCode());
    }
}