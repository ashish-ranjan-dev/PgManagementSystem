package com.pgmanagement.pgservice.model;

public enum RoomType {
    SINGLE,
    DOUBLE,
    TRIPLE,
    OTHER;

    public static RoomType fromBeds(int beds) {
        return switch (beds) {
            case 1 -> SINGLE;
            case 2 -> DOUBLE;
            case 3 -> TRIPLE;
            default -> OTHER;
        };
    }
}