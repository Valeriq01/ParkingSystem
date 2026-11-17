package ParkingSystem.composite;

import java.util.List;

public class ParkingLot extends ParkingGroup {

    public List<ParkingLevel> getLevels() {
        return getChildrenOfType(ParkingLevel.class);
    }
}