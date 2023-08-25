package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.common.image.ImageUploadService;
import com.bb3.bodybuddybe.matching.enums.GenderEnum;
import com.bb3.bodybuddybe.user.dto.ProfileResponseDto;
import com.bb3.bodybuddybe.user.dto.ProfileUpdateRequestDto;
import com.bb3.bodybuddybe.user.dto.SignupRequestDto;
import com.bb3.bodybuddybe.user.dto.UserStatusRequestDto;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.enums.UserRoleEnum;
import com.bb3.bodybuddybe.user.enums.UserStatusEnum;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploadService imageUploadService;

    @Override
    @Transactional
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String birthDate = requestDto.getBirthDate();
        GenderEnum gender = requestDto.getGender();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_USERNAME);
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        User user = new User(username, password, email, birthDate, gender, UserRoleEnum.USER);

        if (user.getAge() < 14) {
            throw new CustomException(ErrorCode.UNDER_AGE);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeStatus(UserStatusRequestDto requestDto, User user) {
        if (user.getStatus() == requestDto.getStatus()) {
            throw new CustomException(ErrorCode.STATUS_NOT_CHANGED);
        }

        if (user.getStatus() == UserStatusEnum.BLOCKED) {
            throw new CustomException(ErrorCode.USER_BLOCKED);
        }

        if (!user.getPassword().equals(passwordEncoder.encode(requestDto.getPassword()))) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        user.changeStatus(requestDto.getStatus());
    }

    @Override
    @Transactional
    public void uploadProfileImage(MultipartFile file, User user) {
        String name = "user/" + user.getId();
        String imageUrl = imageUploadService.upload(file, name);

        user.updateProfileImage(imageUrl);
    }

    @Override
    @Transactional
    public void updateProfile(ProfileUpdateRequestDto requestDto, User user) {
        user.updateProfile(requestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long userId) {
        User user = findById(userId);
        return new ProfileResponseDto(user);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
