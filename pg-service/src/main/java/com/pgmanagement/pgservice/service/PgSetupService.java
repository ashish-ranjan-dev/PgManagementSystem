package com.pgmanagement.pgservice.service;

import com.pgmanagement.pgservice.dto.FloorDto;
import com.pgmanagement.pgservice.dto.PgSetupRequest;
import com.pgmanagement.pgservice.dto.PgSetupResponse;
import com.pgmanagement.pgservice.dto.RoomDto;
import com.pgmanagement.pgservice.model.Building;
import com.pgmanagement.pgservice.model.Floor;
import com.pgmanagement.pgservice.model.Room;
import com.pgmanagement.pgservice.model.RoomType;
import com.pgmanagement.pgservice.repository.BuildingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PgSetupService {

    private final BuildingRepository buildingRepository;

    @Transactional
    public PgSetupResponse setupPg(PgSetupRequest request) {

        // 1. Prevent duplicate setup for the same owner
        if (buildingRepository.existsByOwnerId(request.getOwnerId())) {
            throw new IllegalStateException(
                    "PG already exists for ownerId=" + request.getOwnerId()
            );
        }

        // 2. Build the aggregate: Building -> Floors -> Rooms
        Building building = new Building();
        building.setName(request.getPgName());
        building.setAddress(request.getPgAddress());
        building.setOwnerId(request.getOwnerId());

        int totalRooms = 0;
        for (FloorDto floorDto : request.getFloors()) {
            Floor floor = new Floor();
            floor.setFloorNumber(floorDto.getFloorNumber());

            for (RoomDto roomDto : floorDto.getRooms()) {
                Room room = new Room();
                room.setRoomNumber(roomDto.getRoomNumber());
                room.setBeds(roomDto.getBeds());
                room.setType(RoomType.fromBeds(roomDto.getBeds()));
                floor.addRoom(room);
                totalRooms++;
            }

            building.addFloor(floor);
        }

        // 3. Single save — JPA cascades to floors and rooms
        Building saved = buildingRepository.save(building);

        log.info("PG setup complete: buildingId={}, floors={}, rooms={}, ownerId={}",
                saved.getId(),
                saved.getFloors().size(),
                totalRooms,
                saved.getOwnerId());

        return new PgSetupResponse(
                saved.getId(),
                saved.getFloors().size(),
                totalRooms
        );
    }
}