package com.example.unitechproject.service;

import com.example.unitechproject.exception.TokenExpired;
import com.example.unitechproject.exception.TokenNotFound;
import com.example.unitechproject.model.dto.refreshtoken.TokenRefreshRequestDto;
import com.example.unitechproject.model.dto.refreshtoken.TokenRefreshResponseDto;
import com.example.unitechproject.model.entity.User;
import com.example.unitechproject.model.entity.UserRefreshToken;
import com.example.unitechproject.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    private final long refreshTokenDurationMs = 7L * 24 * 60 * 60 * 1000;

    @Transactional
    public UserRefreshToken createRefreshToken(User user) {
        deleteByUser(user);
        UserRefreshToken refreshToken = UserRefreshToken.builder().user(user).token(UUID.randomUUID().toString()).expiryDate(Instant.now().plusMillis(refreshTokenDurationMs)).build();
        return refreshTokenRepository.save(refreshToken);
    }


    public boolean isExpired(UserRefreshToken refreshToken) {
        return refreshToken.getExpiryDate().isBefore(Instant.now());
    }

    public TokenRefreshResponseDto refreshToken(TokenRefreshRequestDto tokenRefresh) {
        return refreshTokenRepository.findRefreshTokenByToken(tokenRefresh.getRefreshToken()).map(token -> {
            if (isExpired(token)) {
                refreshTokenRepository.delete(token);
                throw new TokenExpired("Refresh token expired. Please log in again.");
            }
            User user = token.getUser();
            String newAccessToken = jwtService.generatedToken(user);
            TokenRefreshResponseDto tokenRefreshResponseDto = new TokenRefreshResponseDto();
            tokenRefreshResponseDto.setRefreshToken(token.getToken());
            tokenRefreshResponseDto.setAccessToken(newAccessToken);
            return tokenRefreshResponseDto;

        }).orElseThrow(() -> new TokenNotFound("Refresh Token Not Found"));
    }

    public String logOut(TokenRefreshRequestDto tokenRefreshRequestDto) {
        return refreshTokenRepository.findRefreshTokenByToken(tokenRefreshRequestDto.getRefreshToken()).map(refreshToken -> {
            deleteByUser(refreshToken.getUser());
            return "User logged out successfully";
        }).orElseThrow(() -> new TokenNotFound("Refresh Token Not Found"));
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
