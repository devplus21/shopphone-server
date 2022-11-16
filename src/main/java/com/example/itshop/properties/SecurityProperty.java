package com.example.itshop.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SecurityProperty {
    @Value("${security.access-token.secret}")
    private String accessTokenSecret;

    @Value("${security.access-token.expires-in}")
    private Long accessTokenExpiresIn;

    @Value("${security.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${security.refresh-token.expires-in}")
    private String refreshTokenExpiresIn;

    @Value("${security.verification.secret.expires-in}")
    private Long verificationSecretExpiresIn;

    @Value("${security.recovery.secret.expires-in}")
    private Long recoverySecretExpiresIn;

}
