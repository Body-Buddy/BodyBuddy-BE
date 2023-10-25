
package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.media.service.AwsS3Service;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.SignupRequestDto;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AwsS3Service awsS3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("유저가 회원가입에 성공한다.")
    void signup_success() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword");
        requestDto.setGender(GenderEnum.F);
        requestDto.setBirthDate(LocalDate.of(2000, 01, 01));

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        // when
        userService.signup(requestDto);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();
        assertEquals(requestDto.getEmail(), user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(requestDto.getGender(), user.getGender());
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthDate());
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 이메일인 경우 예외를 발생시킨다.")
    void signup_emailAlreadyExists() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword");
        requestDto.setGender(GenderEnum.F);
        requestDto.setBirthDate(LocalDate.of(2000, 01, 01));

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(mock(User.class)));

        // when
        CustomException thrownException = assertThrows(CustomException.class, () ->
                userService.signup(requestDto)
        );

        // then
        verify(userRepository, never()).save(any(User.class));
        assertEquals(ErrorCode.DUPLICATED_EMAIL, thrownException.getErrorCode());
    }

    @Test
    @DisplayName("14세 미만의 유저는 회원가입에 실패한다.")
    void signup_ageUnder14() {
        // given
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setEmail("test@email.com");
        requestDto.setPassword("testPassword");
        requestDto.setGender(GenderEnum.M);
        requestDto.setBirthDate(LocalDate.of(2020, 01, 01));

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(requestDto.getPassword())).thenReturn("encodedPassword");

        // when
        CustomException thrownException = assertThrows(CustomException.class, () ->
                userService.signup(requestDto)
        );

        // then
        verify(userRepository, never()).save(any(User.class));
        assertEquals(ErrorCode.UNDER_AGE, thrownException.getErrorCode());
    }

    @Test
    @DisplayName("유저의 프로필 이미지를 성공적으로 업로드한다.")
    void uploadProfileImage_success() {
        // given
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);

        // when
        userService.uploadProfileImage(mock(MultipartFile.class), user);

        // then
        String imageUrl = verify(awsS3Service).uploadFile(any(MultipartFile.class));
        verify(user).updateImageUrl(imageUrl);
    }
}
