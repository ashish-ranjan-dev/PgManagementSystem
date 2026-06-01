package com.pgmanagement.authservice.controller;

import com.pgmanagement.authservice.dto.SigninRequest;
import com.pgmanagement.authservice.dto.SigninResponse;
import com.pgmanagement.authservice.dto.SignupRequest;
import com.pgmanagement.authservice.dto.SignupResponse;
import com.pgmanagement.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/signup")
    public ResponseEntity<SignupResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/v1/signin")
    public ResponseEntity<SigninResponse> signin(
            @Valid @RequestBody SigninRequest request
    ) {
        SigninResponse response = authService.signin(
                request.getPhone(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }
}