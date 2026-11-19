package ParkingSystem.observer;

public record VehicleParkedEvent(
        Long sessionId,
        String licensePlate,
        int allocatedSpots
) {
}
