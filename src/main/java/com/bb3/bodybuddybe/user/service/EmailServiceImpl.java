package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.user.dto.EmailRequestDto;
import com.bb3.bodybuddybe.user.dto.EmailVerificationRequestDto;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final StringRedisTemplate redisTemplate;
    private final Random random = new SecureRandom(); // 보안에 더 강력한 난수 생성
    private static final int CODE_LENGTH = 6;
    private static final String CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendVerificationCode(EmailRequestDto requestDto) {
        String email = requestDto.getEmail();
        String code = generateVerificationCode();
        redisTemplate.opsForValue().set(email, code, 10, TimeUnit.MINUTES);

        String subject = "바디버디 회원가입 이메일 인증";
        String htmlContent = "<div style='margin:100px;'>" +
                "<h1>안녕하세요, 바디버디 가입을 환영합니다!</h1>" +
                "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요.</p>" +
                "<div align='center' style='border:1px solid black; font-family:verdana';>" +
                "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>" +
                "<div style='font-size:130%'><strong> +" + code + "</strong></div></div>" +
                "</div>";

        sendHtmlMessage(email, subject, htmlContent);
    }

    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTER_SET.charAt(random.nextInt(CHARACTER_SET.length())));
        }

        return code.toString();
    }

    private void sendHtmlMessage(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.addRecipients(Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setText(htmlContent, "UTF-8", "html");
            message.setFrom(new InternetAddress(from, "BodyBuddy Admin"));
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("failed to send email", e);
            throw new CustomException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }

    @Override
    public void verifyCode(EmailVerificationRequestDto requestDto) {
        String email = requestDto.getEmail();
        String code = requestDto.getVerificationCode();
        String storedCode = redisTemplate.opsForValue().get(email);

        if (storedCode == null || !storedCode.equals(code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisTemplate.delete(email);
    }
}

