package ParkingSystem.model.entity;

import ParkingSystem.composite.ParkingComponent;
import ParkingSystem.model.enums.SpotStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_spots",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "level_number",
                        "parking_row",
                        "spot_number"
                }
        ))
public class ParkingSpot implements ParkingComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_number", nullable = false)
    private int levelNumber;

    @Column(name = "parking_row", nullable = false)
    private int rowNumber;

    @Column(name = "spot_number", nullable = false)
    private int spotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private SpotStatus status = SpotStatus.FREE;

    @ManyToOne(fetch = FetchType.LAZY)
    private ParkingSession session;

    protected ParkingSpot() {
    }

    public ParkingSpot(int levelNumber, int rowNumber, int spotNumber) {
        this.levelNumber = levelNumber;
        this.rowNumber = rowNumber;
        this.spotNumber = spotNumber;
        this.status = SpotStatus.FREE;
    }

    public Long getId() {
        return id;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public ParkingSession getSession() {
        return session;
    }

    public void occupy(ParkingSession session) {
        this.session = session;
        this.status = SpotStatus.OCCUPIED;
    }

    public void release() {
        this.session = null;
        this.status = SpotStatus.FREE;
    }

    @Override
    public int getTotalSpots() {
        return 1;
    }

    @Override
    public int getFreeSpots() {
        return status == SpotStatus.FREE ? 1 : 0;
    }

    public String getDisplayName() {
        return "Н" + levelNumber + " · Р" + rowNumber + " · М" + spotNumber;
    }
}
