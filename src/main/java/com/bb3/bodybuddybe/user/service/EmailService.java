package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.user.dto.EmailRequestDto;
import com.bb3.bodybuddybe.user.dto.EmailConfirmRequestDto;

public interface EmailService {
    void sendVerificationCode(EmailRequestDto requestDto);

    void confirmVerification(EmailConfirmRequestDto requestDto);




}

