package com.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.security.JwtTokenProvider;
import com.web.service.UserService;
import com.web.user.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public Map<String, String> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            String userId = loginRequest.get("userId");
            String password = loginRequest.get("userPassword");

            logger.info("Attempting authentication for userId: {}", userId);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password));

            String token = tokenProvider.generateToken(authentication.getName());

            response.put("token", token);
            response.put("message", "Login success");
            logger.info("Authentication successful for userId: {}", userId);
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for userId: {}", loginRequest.get("userId"), e);
            response.put("message", "Invalid username or password");
        } catch (Exception e) {
            logger.error("An error occurred during authentication", e);
            response.put("message", "An error occurred: " + e.getMessage());
        }

        return response;
    }
    
    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestBody User user) {
        logger.info("Received registration request for user: {}", user);
        Map<String, String> response = new HashMap<>();
        try {
            userService.registerUser(user);
            response.put("message", "Registration successful");
        } catch (Exception e) {
            logger.error("An error occurred during registration: {}", e.getMessage());
            response.put("message", "An error occurred: " + e.getMessage());
        }
        return response;
    }
}
