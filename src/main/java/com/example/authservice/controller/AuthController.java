package com.example.authservice.controller;

import com.example.authservice.entity.LoginRequest;
import com.example.authservice.entity.RegisterRequest;
import com.example.authservice.entity.Token;
import com.example.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @QueryMapping
    public Token loginUser(@Argument String email, @Argument String password){
        LoginRequest loginRequest = new LoginRequest(email, password);
        try{
            Token token = this.authService.loginUser(loginRequest);
            return token;
        } catch (Exception e) {
//            throw new Exception("Login unsuccessful");
            return null;
        }
    }

    @MutationMapping
    public Token registerUser(@Argument String username, @Argument String email, @Argument String password)  {
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        System.out.println(registerRequest);
        try{
            Token token = this.authService.registerUser(registerRequest);
            return token;
        } catch (Exception e) {
         return null;
        }
    }

    @QueryMapping
    public Boolean isTokenExpired(@Argument String accessToken) {
        return this.authService.isTokenExpired(accessToken);
    }
}
