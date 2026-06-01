package com.pgmanagement.pgservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PgSetupResponse {

    private Long buildingId;
    private Integer floorsCreated;
    private Integer roomsCreated;
}