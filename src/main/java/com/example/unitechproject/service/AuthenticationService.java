package com.example.unitechproject.service;


import com.example.unitechproject.exception.VerifyCodeFailedException;
import com.example.unitechproject.exception.CodeExpireException;
import com.example.unitechproject.model.dto.auth.AuthenticationRequestDto;
import com.example.unitechproject.exception.EmailNotFound;
import com.example.unitechproject.model.dto.auth.AuthenticationResponseDto;
import com.example.unitechproject.model.dto.code.AuthCodeVerficationDto;
import com.example.unitechproject.model.dto.userDto.UserRequestDto;
import com.example.unitechproject.model.entity.Role;
import com.example.unitechproject.model.entity.User;
import com.example.unitechproject.repository.UserRepository;
import com.example.unitechproject.service.notification.EmailService;
import com.example.unitechproject.service.notification.VerificationCodeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final VerificationCodeStore verificationCodeStore;
    private final UserRefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public String userRegister(UserRequestDto request) {
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return "Register Successfully";
    }
    public String authenticateSendCode(AuthenticationRequestDto request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()){
        String code = String.valueOf(new Random().nextInt(900000)+100000);
        verificationCodeStore.saveCode(request.getEmail(),code);
        emailService.sendVerificationCode(request.getEmail(),code);
        return "Verification Code Send";
        }else {
            throw new EmailNotFound("Not Found");
        }
    }

    public AuthenticationResponseDto verifyCode(AuthCodeVerficationDto authCodeVerfication) {
        if (verificationCodeStore.isCodeValid(authCodeVerfication.getEmail(), authCodeVerfication.getCode())) {
            verificationCodeStore.removeCode(authCodeVerfication.getEmail());
            Optional<User> user = userRepository.findByEmail(authCodeVerfication.getEmail());
            if (user.isPresent()){
                userRepository.save(user.get());
                var jwtToken = jwtService.generatedToken(user.get());
                String refreshToken = refreshTokenService.createRefreshToken(user.get()).getToken();
                return AuthenticationResponseDto.builder().token(jwtToken).refreshToken(refreshToken).build();
            }else {
                throw new VerifyCodeFailedException("Email or Code is wrong");
            }
        } else {
            throw new CodeExpireException("Code expire try to send code again");
        }
    }
}
