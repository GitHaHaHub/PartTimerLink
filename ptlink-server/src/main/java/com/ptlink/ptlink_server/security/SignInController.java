package com.ptlink.ptlink_server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RestController
@AllArgsConstructor
public class SigninController {

    private final AuthenticationManager authenticationManager;


    @Autowired
    private JwtService jwtService;

    @PostMapping("/SignIn")
    public ResponseEntity<String> signIn(@RequestBody AuthRequest authRequest, HttpServletResponse response)
    {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok()
            .body("{\"Authorization\": \"" + jwtService.generateAccessToken(authRequest.username(),
            Map.of("role", authentication.getAuthorities().iterator().next().getAuthority())
            ) + "\"}");
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    public record AuthRequest(String username, String password) {
	}
}