package dev.malay.productservicejuly26.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType;
    private long expiresIn;
}
