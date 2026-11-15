package ParkingSystem.repository;

import ParkingSystem.model.entity.ParkingSpot;
import ParkingSystem.model.enums.SpotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpotRepository
        extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findAllByOrderByLevelNumberAscRowNumberAscSpotNumberAsc();

    List<ParkingSpot> findByStatusOrderByLevelNumberAscRowNumberAscSpotNumberAsc(
            SpotStatus status
    );

    List<ParkingSpot> findBySession_Id(Long sessionId);

    long countByStatus(SpotStatus status);
}
