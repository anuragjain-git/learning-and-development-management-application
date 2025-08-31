package com.vilt.narmada.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.vilt.narmada.dto.LoginRequest;
import com.vilt.narmada.dto.RegisterRequest;
import com.vilt.narmada.model.User;
import com.vilt.narmada.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<User> register(@Valid @RequestBody User user) {
//        return ResponseEntity.ok(userService.register(user));
//    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request, BindingResult bindingResult) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setActive(true);

        User savedUser = userService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Valid @RequestBody LoginRequest login) {
        User user = userService.login(login.getEmail(), login.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // not asked in question, added to have a default get endpoint for the path /api/v1/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
