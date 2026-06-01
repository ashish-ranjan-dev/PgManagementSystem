package com.pgmanagement.pgservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "rooms",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_room_floor_number",
                columnNames = {"floor_id", "room_number"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private Integer beds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;
}