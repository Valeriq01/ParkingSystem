package ParkingSystem.composite;

import java.util.List;

public class ParkingLevel extends ParkingGroup {

    private final int levelNumber;

    public ParkingLevel(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public List<ParkingRow> getRows() {
        return getChildrenOfType(ParkingRow.class);
    }
}