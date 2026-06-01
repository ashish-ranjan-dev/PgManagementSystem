package com.pgmanagement.pgservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorDto {

    @NotNull(message = "Floor number is required")
    @Min(value = 0, message = "Floor number must be >= 0")
    private Integer floorNumber;

    @NotEmpty(message = "Floor must have at least one room")
    @Valid
    private List<RoomDto> rooms;
}