package com.example.SecureAndBox.login.dto.request;



import com.example.SecureAndBox.login.domain.enums.Provider;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(@NotNull Provider provider, String name) {
}