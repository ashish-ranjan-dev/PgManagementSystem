package com.pgmanagement.pgservice.controller;

import com.pgmanagement.pgservice.dto.PgSetupRequest;
import com.pgmanagement.pgservice.dto.PgSetupResponse;
import com.pgmanagement.pgservice.service.PgSetupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pg")
@RequiredArgsConstructor
public class PgController {

    private final PgSetupService pgSetupService;

    @PostMapping("/v1/setup")
    public ResponseEntity<PgSetupResponse> setup(
            @Valid @RequestBody PgSetupRequest request
    ) {
        PgSetupResponse response = pgSetupService.setupPg(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}