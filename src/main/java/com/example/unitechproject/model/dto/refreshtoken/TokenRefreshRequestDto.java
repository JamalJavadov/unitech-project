package com.example.unitechproject.model.dto.refreshtoken;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenRefreshRequestDto {
    private String refreshToken;
}
