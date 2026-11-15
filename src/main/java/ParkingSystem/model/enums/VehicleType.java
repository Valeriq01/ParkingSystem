package ParkingSystem.model.enums;

public enum VehicleType {
    MOTORCYCLE("Мотоциклет", 1),
    CAR("Автомобил", 2),
    TRUCK("Камион", 4);

    private final String label;
    private final int requiredSpots;

    VehicleType(String label, int requiredSpots) {
        this.label = label;
        this.requiredSpots = requiredSpots;
    }

    public String getLabel() { return label; }
    public int getRequiredSpots() { return requiredSpots; }
}
