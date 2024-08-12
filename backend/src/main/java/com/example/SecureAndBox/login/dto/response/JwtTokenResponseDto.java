package com.example.SecureAndBox.login.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtTokenResponseDto {

    @NotNull
    @JsonProperty("accessToken")
    private String accessToken;

    @NotNull
    @JsonProperty("refreshToken")
    private String refreshToken;
}
