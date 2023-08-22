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
import com.bb3.bodybuddybe.matching.enums.ExerciseTimeEnum;
import com.bb3.bodybuddybe.matching.enums.ExperienceEnum;
import com.bb3.bodybuddybe.matching.enums.GoalEnum;
import com.bb3.bodybuddybe.matching.enums.IntensityEnum;
import com.bb3.bodybuddybe.matching.repository.MatchingCriteriaRepository;
import com.bb3.bodybuddybe.user.dto.UserProfileDto;
import com.bb3.bodybuddybe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchingServiceImplTest {
    @InjectMocks
    private MatchingServiceImpl matchingService;

    @Mock
    private UserGymRepository userGymRepository;

    @Mock
    private MatchingCriteriaRepository matchingCriteriaRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자의 매칭 기준을 생성한다.")
    void testCreateMatchingCriteria() {
        // given
        User user = new User();
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
        CriteriaUpdateRequestDto requestDto = Mockito.mock(CriteriaUpdateRequestDto.class);
        MatchingCriteria criteria = Mockito.mock(MatchingCriteria.class);

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
        Gym gym = Mockito.mock(Gym.class);

        User currentUser = new User();
        User other1 = new User();
        User other2 = new User();

        UserGym userGym1 = new UserGym(currentUser, gym);
        UserGym userGym2 = new UserGym(other1, gym);
        UserGym userGym3 = new UserGym(other2, gym);

        ReflectionTestUtils.setField(currentUser, "id", 1L);
        ReflectionTestUtils.setField(other1, "id", 2L);
        ReflectionTestUtils.setField(other2, "id", 3L);

        List<UserGym> allUsersInGym = List.of(userGym1, userGym2, userGym3);

        when(userGymRepository.existsByUserAndGymId(currentUser, gymId)).thenReturn(true);
        when(userGymRepository.findAllByGymId(gymId)).thenReturn(allUsersInGym);

        when(matchingCriteriaRepository.findByUser(currentUser)).thenReturn(Optional.of(mock(MatchingCriteria.class)));
        when(matchingCriteriaRepository.findByUser(other1)).thenReturn(Optional.of(mock(MatchingCriteria.class)));
        when(matchingCriteriaRepository.findByUser(other2)).thenReturn(Optional.of(mock(MatchingCriteria.class)));

        // when
        List<UserProfileDto> matchingUsers = matchingService.getMatchingUsers(gymId, currentUser);

        // then
        verify(userGymRepository).findAllByGymId(gymId);
        assertEquals(2, matchingUsers.size());
        assertTrue(matchingUsers.stream().noneMatch(userProfile -> userProfile.getId().equals(currentUser.getId())));
    }

    @Test
    @DisplayName("사용자가 등록하지 않은 헬스장의 매칭 유저를 조회할 때 에러를 발생시킨다.")
    public void testGetMatchingUsers_failure() {
        // given
        Long gymId = 1L;
        User currentUser = Mockito.mock(User.class);

        when(userGymRepository.existsByUserAndGymId(currentUser, gymId)).thenReturn(false);

        // when
        CustomException thrownException = assertThrows(CustomException.class, () -> {
            matchingService.getMatchingUsers(gymId, currentUser);
        });

        // then
        verify(userGymRepository).existsByUserAndGymId(currentUser, gymId);
        verify(userGymRepository, never()).findAllByGymId(gymId);
        assertEquals(ErrorCode.NOT_MY_GYM, thrownException.getErrorCode());
    }
}