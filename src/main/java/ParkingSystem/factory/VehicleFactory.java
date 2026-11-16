package ParkingSystem.factory;

import ParkingSystem.model.entity.Vehicle;
import ParkingSystem.model.enums.VehicleType;
import org.springframework.stereotype.Component;

@Component
public class VehicleFactory {

    public Vehicle create(String licensePlate, VehicleType type) {
        return new Vehicle(licensePlate, type);
    }
}