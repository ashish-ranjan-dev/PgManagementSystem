package com.pgmanagement.authservice.service;

import com.pgmanagement.authservice.client.PgServiceClient;
import com.pgmanagement.authservice.dto.*;
import com.pgmanagement.authservice.exception.AuthExceptions.InvalidCredentialsException;
import com.pgmanagement.authservice.exception.AuthExceptions.PhoneAlreadyExistsException;
import com.pgmanagement.authservice.exception.AuthExceptions.PgServiceException;
import com.pgmanagement.authservice.model.Role;
import com.pgmanagement.authservice.model.User;
import com.pgmanagement.authservice.repository.UserRepository;
import com.pgmanagement.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PgServiceClient pgServiceClient;

    @Value("${app.pg-service.internal-api-key}")
    private String internalApiKey;

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // 1. Business rule: passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        // 2. Check phone uniqueness
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }

        // 3. Hash password (never store raw)
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // 4. Create user
        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setPasswordHash(passwordHash);
        user.setRole(Role.OWNER);

        User saved = userRepository.save(user);
        log.info("User created with id={}, phone=***{}", saved.getId(),
                tail(saved.getPhone()));

        // 5. Call PG service — compensating rollback if it fails
        try {
            PgSetupRequest pgRequest = new PgSetupRequest(
                    saved.getId(),
                    request.getPgName(),
                    request.getPgAddress(),
                    request.getFloors()
            );
            PgSetupResponse pgResponse = pgServiceClient.setupPg(internalApiKey, pgRequest);
            log.info("PG setup complete: buildingId={}, rooms={}",
                    pgResponse.getBuildingId(), pgResponse.getRoomsCreated());
        } catch (Exception e) {
            log.error("PG service call failed for userId={}, rolling back user", saved.getId(), e);
            userRepository.delete(saved);
            throw new PgServiceException("PG setup failed, signup rolled back", e);
        }

        // 6. Generate JWT
        String token = jwtUtil.generateToken(saved.getId(), saved.getPhone(),
                saved.getRole().name());

        return new SignupResponse("Signup successful", saved.getId(), token);
    }

    public SigninResponse signin(String phone, String rawPassword) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        log.info("Signin success for userId={}", user.getId());
        String token = jwtUtil.generateToken(user.getId(), user.getPhone(),
                user.getRole().name());

        return new SigninResponse(user.getId(), token);
    }

    /** Returns the last 4 digits of a phone number for safe logging. */
    private String tail(String phone) {
        return phone == null || phone.length() < 4
                ? "????"
                : phone.substring(phone.length() - 4);
    }
}