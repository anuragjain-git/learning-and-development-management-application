package com.vilt.narmada.controller;

import com.vilt.narmada.configuration.JwtUtils;
import com.vilt.narmada.dto.JwtResponse;
import com.vilt.narmada.dto.LoginRequest;
import com.vilt.narmada.model.User;
import com.vilt.narmada.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok("Registration Successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication);
        String message = "Login successful! Welcome.";

        return ResponseEntity.ok(new JwtResponse(message,jwt));
    }
}
