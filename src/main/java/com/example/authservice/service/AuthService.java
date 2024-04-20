package com.example.authservice.service;

import com.example.authservice.entity.LoginRequest;
import com.example.authservice.entity.RegisterRequest;
import com.example.authservice.entity.Token;
import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;

    public Token registerUser(RegisterRequest registerRequest) {
        registerRequest.setPassword(BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt()));
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());

        String accessToken = jwtUtils.generate(registerRequest.getUsername(), registerRequest.getEmail(), "ACCESS");
        String refreshToken = jwtUtils.generate(registerRequest.getUsername(), registerRequest.getEmail(), "REFRESH");

        this.userRepository.save(user);

        return new Token(accessToken, refreshToken);
    }

    public Token loginUser(LoginRequest loginRequest) {
        User user = this.userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (user != null && BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {

            String accessToken = jwtUtils.generate(user.getUsername(), user.getEmail(), "ACCESS");
            String refreshToken = jwtUtils.generate(user.getUsername(), user.getEmail(), "REFRESH");
            return new Token(accessToken, refreshToken);
        }

        return null;
    }

    public Claims getUserDetails(String token) {
        return jwtUtils.getClaims(token);
    }
}
