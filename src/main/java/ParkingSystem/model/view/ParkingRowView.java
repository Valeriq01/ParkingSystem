package ParkingSystem.model.view;

import java.util.List;

public class ParkingRowView {

    private final int rowNumber;
    private final int totalSpots;
    private final int freeSpots;
    private final List<ParkingCellView> cells;

    public ParkingRowView(
            int rowNumber,
            int totalSpots,
            int freeSpots,
            List<ParkingCellView> cells
    ) {
        this.rowNumber = rowNumber;
        this.totalSpots = totalSpots;
        this.freeSpots = freeSpots;
        this.cells = List.copyOf(cells);
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public List<ParkingCellView> getCells() {
        return cells;
    }
}