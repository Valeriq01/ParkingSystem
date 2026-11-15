package ParkingSystem.composite;

public interface ParkingComponent {

    int getTotalSpots();

    int getFreeSpots();

    default int getOccupiedSpots() {
        return getTotalSpots() - getFreeSpots();
    }
}