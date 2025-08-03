package com.example.unitechproject.controller;


import com.example.unitechproject.model.dto.auth.AuthenticationRequestDto;
import com.example.unitechproject.model.dto.auth.AuthenticationResponseDto;
import com.example.unitechproject.model.dto.code.AuthCodeVerficationDto;
import com.example.unitechproject.model.dto.refreshtoken.TokenRefreshRequestDto;
import com.example.unitechproject.model.dto.refreshtoken.TokenRefreshResponseDto;
import com.example.unitechproject.model.dto.userDto.UserRequestDto;
import com.example.unitechproject.service.AuthenticationService;
import com.example.unitechproject.service.UserRefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRefreshTokenService userRefreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(authenticationService.userRegister(request));
    }

    @PostMapping("/send-code")
    public ResponseEntity<String> authenticate(@Valid @RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(authenticationService.authenticateSendCode(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthenticationResponseDto> verifyCode(@Valid @RequestBody AuthCodeVerficationDto authCodeVerfication) {
        return ResponseEntity.ok(authenticationService.verifyCode(authCodeVerfication));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(@RequestBody TokenRefreshRequestDto tokenRefresh) {
        return ResponseEntity.ok(userRefreshTokenService.refreshToken(tokenRefresh));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        return ResponseEntity.ok(userRefreshTokenService.logOut(tokenRefreshRequestDto));
    }
}
