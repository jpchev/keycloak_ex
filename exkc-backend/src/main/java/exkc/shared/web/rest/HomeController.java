package exkc.shared.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/version")
    public ResponseEntity<String> version() {
        return new ResponseEntity<>("1.0.0", HttpStatus.OK);
    }

    @GetMapping(value = "/quisuisje")
    @PreAuthorize("hasRole('ROLE_MIROIR')")
    public ResponseEntity<String> quisuisje() {
        JwtAuthenticationToken token = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt)token.getPrincipal();
        String name = jwt.getClaimAsString("preferred_username");
        return new ResponseEntity<>(name, HttpStatus.OK);
    }

}
