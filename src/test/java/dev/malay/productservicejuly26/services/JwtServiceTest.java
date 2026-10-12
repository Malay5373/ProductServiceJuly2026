package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.models.Role;
import dev.malay.productservicejuly26.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwt = new JwtService(
            "01234567890123456789012345678901", 3_600_000);

    @Test
    void issuedTokenContainsUsernameAndCanBeValidated() {
        User user = new User();
        user.setUsername("alice");
        user.setRole(Role.USER);

        String token = jwt.issue(user);

        assertEquals("alice", jwt.username(token));
    }

    @Test
    void shortSecretIsRejectedWhenIssuingToken() {
        JwtService insecure = new JwtService("too-short", 3_600_000);
        User user = new User();
        user.setUsername("alice");
        user.setRole(Role.USER);

        assertThrows(IllegalStateException.class, () -> insecure.issue(user));
    }
}
