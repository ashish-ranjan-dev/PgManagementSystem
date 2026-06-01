package com.pgmanagement.authservice.client;

import com.pgmanagement.authservice.dto.PgSetupRequest;
import com.pgmanagement.authservice.dto.PgSetupResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "pg-service",
        url = "${app.pg-service.base-url}"
)
public interface PgServiceClient {

    @PostMapping(
            value = "/api/pg/v1/setup",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    PgSetupResponse setupPg(
            @RequestHeader("X-Internal-API-Key") String apiKey,
            @RequestBody PgSetupRequest request
    );
}