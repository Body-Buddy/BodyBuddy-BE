package com.bb3.bodybuddybe.user.service;

import com.bb3.bodybuddybe.common.exception.CustomException;
import com.bb3.bodybuddybe.common.exception.ErrorCode;
import com.bb3.bodybuddybe.user.dto.EmailConfirmRequestDto;
import com.bb3.bodybuddybe.user.dto.EmailRequestDto;
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
        String htmlContent = generateVerificationEmail(code);

        sendHtmlMessage(email, subject, htmlContent);
    }



    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTER_SET.charAt(random.nextInt(CHARACTER_SET.length())));
        }

        return code.toString();
    }

    public String generateVerificationEmail(String verificationCode) {
        String template = """
        <!DOCTYPE html>
        <html lang="ko">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>바디버디 이메일 인증</title>
        </head>
        <body style="font-family: 'Noto Sans KR', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
            <div class="container" style="max-width: 600px; margin: 50px auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
                <div class="header" style="text-align: center; margin-bottom: 20px;">
                    <h2>안녕하세요, 바디버디 가입을 환영합니다!</h2>
                    <p>아래 인증 코드를 입력하여 회원가입을 완료해 주세요 🙌🏻</p>
                </div>
                <span class="code" style="display: block; text-align: center; font-size: 20px; font-weight: bold; margin: 20px 0;">%s</span>
            </div>
        </body>
        </html>
        """;

        return String.format(template, verificationCode);
    }

    private void sendHtmlMessage(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.addRecipients(Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(from, "BodyBuddy Admin"));
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("failed to send email", e);
            throw new CustomException(ErrorCode.EMAIL_SENDING_FAILED);
        }
    }


    @Override
    public void confirmVerification(EmailConfirmRequestDto requestDto) {
        String email = requestDto.getEmail();
        String code = requestDto.getCode();
        String storedCode = redisTemplate.opsForValue().get(email);

        if (storedCode == null || !storedCode.equals(code)) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        redisTemplate.delete(email);
    }
}

