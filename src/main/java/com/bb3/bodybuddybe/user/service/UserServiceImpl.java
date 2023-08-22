package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.advice.ApiResponseDto;
import com.bb3.bodybuddybe.common.image.ImageUploader;
import com.bb3.bodybuddybe.user.UserRoleEnum;
import com.bb3.bodybuddybe.user.dto.*;
import com.bb3.bodybuddybe.user.entity.User;
import com.bb3.bodybuddybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUploader imageUploader;
    // OWNER_TOKEN
    private final String POSTOWNER_TOKEN = "1111";

    @Override
    public void signup(AuthRequestDto requestDto){
        String username = requestDto.getUsername();
        String passwordDecoded = requestDto.getPassword();
        String password = passwordEncoder.encode(requestDto.getPassword()); // 패스워드 평문 암호화
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();


        if(userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException();}

        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException();
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isPostOwner()) {
            if (!POSTOWNER_TOKEN.equals(requestDto.getPostownerToken())) {
                throw new IllegalArgumentException();
            }
            role = UserRoleEnum.POSTOWNER;
        }

        // 사용자 등록
        User user = new User(username,nickname, password, passwordDecoded, email, role);
        userRepository.save(user);
    }




    @Override
    public ResponseEntity<ApiResponseDto> changeUserInfo(MultipartFile profilePic, String introduction, String password, User user) throws IOException {
        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() ->
                new IllegalArgumentException(""));
        if (profilePic != null) {
            String imageUrl = imageUploader.upload(profilePic, "image");
            dbUser.setImageUrl(imageUrl);
        }

        dbUser.setIntroduction(introduction);

        String newPW = password;
        Map<String, String> map = new HashMap<String, String>();
        map.put("pw", user.getPassword());


        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!(entry.getValue() == null)) {
                if (passwordEncoder.matches(newPW, entry.getValue())) {
                    throw new IllegalArgumentException("이전 비밀번호와 일치합니다.");
                }
            } else {
                break;
            }
        }

        dbUser.setPassword(passwordEncoder.encode(newPW));
        dbUser.setPasswordDecoded(newPW);

        ApiResponseDto apiResponseDto = new ApiResponseDto("프로필이 변경되었습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }


    public boolean isValidString(String input){
        if (input.length() < 8 || input.length() > 15) {
            return false;
        }
        String regex = "^[a-zA-Z0-9!@#$%^&*()-_=+\\[\\]{}|;:',.<>/?]*$";
        return Pattern.matches(regex, input);
    }

    @Override
    public UserProfileDto getUserProfile(User user) {
        User users = userRepository.findById(user.getId()).orElseThrow(()-> new IllegalArgumentException("회원정보가 없습니다."));
        return new UserProfileDto(users);
    }

    @Override
    public ResponseEntity<ProfileResponseDto> getProfile(User user) {
        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() ->
                new IllegalArgumentException(""));
        ProfileResponseDto profileResponseDto = new ProfileResponseDto(dbUser.getImageUrl(), dbUser.getIntroduction());
        return new ResponseEntity<>(profileResponseDto, HttpStatus.OK);
    }


    @Override
    public void delete(DeleteRequestDto requestDto, User user){
        if (!requestDto.getPassword().equals(user.getPasswordDecoded())) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }
        userRepository.delete(user);
    }
}
