package ParkingSystem.model.view;

import java.util.List;

public class ParkingLevelView {

    private final int levelNumber;
    private final int totalSpots;
    private final int freeSpots;
    private final List<ParkingRowView> rows;

    public ParkingLevelView(
            int levelNumber,
            int totalSpots,
            int freeSpots,
            List<ParkingRowView> rows
    ) {
        this.levelNumber = levelNumber;
        this.totalSpots = totalSpots;
        this.freeSpots = freeSpots;
        this.rows = List.copyOf(rows);
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public List<ParkingRowView> getRows() {
        return rows;
    }
}