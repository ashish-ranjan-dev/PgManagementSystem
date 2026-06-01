package com.pgmanagement.pgservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PgSetupRequest {

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotBlank(message = "PG name is required")
    private String pgName;

    @NotBlank(message = "PG address is required")
    private String pgAddress;

    @NotEmpty(message = "At least one floor is required")
    @Valid
    private List<FloorDto> floors;
}