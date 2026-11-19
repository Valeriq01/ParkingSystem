package ParkingSystem.observer;

public record VehicleExitedEvent(
        Long sessionId,
        String licensePlate
) {
}