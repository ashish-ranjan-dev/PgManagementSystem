package com.pgmanagement.authservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Beds count is required")
    @Min(value = 1, message = "Beds must be at least 1")
    private Integer beds;
}