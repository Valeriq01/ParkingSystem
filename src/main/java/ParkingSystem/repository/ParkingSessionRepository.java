package ParkingSystem.repository;

import ParkingSystem.model.entity.ParkingSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository
        extends JpaRepository<ParkingSession, Long> {

    @EntityGraph(attributePaths = {"vehicle", "spots"})
    List<ParkingSession> findAllByOrderByEntryTimeDesc();

    @EntityGraph(attributePaths = {"vehicle", "spots"})
    List<ParkingSession> findByExitTimeIsNullOrderByEntryTimeDesc();

    Optional<ParkingSession> findByIdAndExitTimeIsNull(Long id);
}

