package exkc.api.config.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthentificationServiceTest {
    @InjectMocks
    AuthentificationService authentificationService;
    @Mock
    private SecurityContext securityContext;


    @Test
    void getNom() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("clientId", "exkc-app");

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(60);
        Jwt jwt = new Jwt("tokenValue", issuedAt, expiresAt, headers, claims);

        when(securityContext.getAuthentication()).thenReturn(new JwtAuthenticationToken(jwt));
        SecurityContextHolder.setContext(securityContext);
        assertThat(authentificationService.getCurrentUser().getClientId()).isEqualTo("exkc-app");
    }
}
