package ParkingSystem.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ParkingGroup implements ParkingComponent {

    private final List<ParkingComponent> children = new ArrayList<>();

    public void add(ParkingComponent component) {
        children.add(Objects.requireNonNull(component));
    }

    public List<ParkingComponent> getChildren() {
        return List.copyOf(children);
    }

    protected <T extends ParkingComponent> List<T> getChildrenOfType(Class<T> type) {
        return children.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    @Override
    public int getTotalSpots() {
        return children.stream()
                .mapToInt(ParkingComponent::getTotalSpots)
                .sum();
    }

    @Override
    public int getFreeSpots() {
        return children.stream()
                .mapToInt(ParkingComponent::getFreeSpots)
                .sum();
    }
}