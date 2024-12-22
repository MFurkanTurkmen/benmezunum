package com.mft.benmezunum.controller;

import com.mft.benmezunum.dto.request.LoginRequest;
import com.mft.benmezunum.dto.request.SignupTeacherRQ;
import com.mft.benmezunum.dto.response.LoginResponse;
import com.mft.benmezunum.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup-teacher")
    public ResponseEntity<?> registerUser(@RequestBody SignupTeacherRQ signUpTeacherRQ, HttpServletRequest request) {
        authService.register(signUpTeacherRQ,request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }



}

