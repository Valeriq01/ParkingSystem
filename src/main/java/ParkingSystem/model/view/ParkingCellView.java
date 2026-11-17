package ParkingSystem.model.view;

public class ParkingCellView {

    private final boolean vehicle;
    private final int spotNumber;
    private final int span;

    private final Long sessionId;
    private final String licensePlate;
    private final String vehicleType;
    private final String vehicleLabel;
    private final String entryTime;

    public ParkingCellView(int spotNumber) {
        this.vehicle = false;
        this.spotNumber = spotNumber;
        this.span = 1;

        this.sessionId = null;
        this.licensePlate = null;
        this.vehicleType = null;
        this.vehicleLabel = null;
        this.entryTime = null;
    }

    public ParkingCellView(
            int spotNumber,
            Long sessionId,
            String licensePlate,
            String vehicleType,
            String vehicleLabel,
            String entryTime,
            int span
    ) {
        this.vehicle = true;
        this.spotNumber = spotNumber;
        this.span = span;

        this.sessionId = sessionId;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.vehicleLabel = vehicleLabel;
        this.entryTime = entryTime;
    }

    public boolean isVehicle() {
        return vehicle;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public int getSpan() {
        return span;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleLabel() {
        return vehicleLabel;
    }

    public String getEntryTime() {
        return entryTime;
    }
}
