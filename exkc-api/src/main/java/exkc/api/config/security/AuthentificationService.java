package exkc.api.config.security;

import exkc.api.config.security.dto.UserDto;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {
    public UserDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        JwtAuthenticationToken principal = ((JwtAuthenticationToken) auth);
        String clientId = principal.getToken().getClaims().get("clientId").toString();

        UserDto user = new UserDto();
        user.setClientId(clientId);
        return user;
    }
}
