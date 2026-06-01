package com.pgmanagement.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PgSetupRequest {

    private Long ownerId;
    private String pgName;
    private String pgAddress;
    private List<FloorDto> floors;
}