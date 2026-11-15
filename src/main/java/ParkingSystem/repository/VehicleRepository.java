package ParkingSystem.repository;

import ParkingSystem.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository
        extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlateIgnoreCase(String licensePlate);
}

