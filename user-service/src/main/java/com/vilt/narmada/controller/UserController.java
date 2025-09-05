package com.vilt.narmada.controller;

import com.vilt.narmada.dto.UserIdRequest;
import com.vilt.narmada.model.User;
import com.vilt.narmada.service.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/id")
    public ResponseEntity<UserIdRequest> getUserIdByEmail(@RequestParam String email) {
        Long userId = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(new UserIdRequest(userId));
    }
}