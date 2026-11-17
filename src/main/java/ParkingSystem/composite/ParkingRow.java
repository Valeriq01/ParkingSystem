package ParkingSystem.composite;

import ParkingSystem.model.entity.ParkingSpot;

import java.util.List;

public class ParkingRow extends ParkingGroup {

    private final int rowNumber;

    public ParkingRow(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public List<ParkingSpot> getSpots() {
        return getChildrenOfType(ParkingSpot.class);
    }
}