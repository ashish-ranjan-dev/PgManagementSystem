package com.pgmanagement.pgservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "floors",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_floor_building_number",
                columnNames = {"building_id", "floor_number"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
        room.setFloor(this);
    }
}