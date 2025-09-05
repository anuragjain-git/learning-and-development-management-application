package com.vilt.narmada.service;

import com.vilt.narmada.exception.EmailAlreadyExistsException;
import com.vilt.narmada.exception.InvalidCredentialsException;
import com.vilt.narmada.exception.InvalidEmailException;
import com.vilt.narmada.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vilt.narmada.model.User;
import com.vilt.narmada.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(User user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistsException(user.getEmail());
        });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public User login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(InvalidCredentialsException::new);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(InvalidEmailException::new);
    }
}

