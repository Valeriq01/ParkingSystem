package ParkingSystem.model.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_sessions")
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @OneToMany(mappedBy = "session")
    private List<ParkingSpot> spots = new ArrayList<>();

    protected ParkingSession() {
    }

    public ParkingSession(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public boolean isActive() {
        return exitTime == null;
    }

    public void close() {
        this.exitTime = LocalDateTime.now();
    }

    public long getDurationMinutes() {
        return Duration.between(
                entryTime,
                exitTime == null ? LocalDateTime.now() : exitTime
        ).toMinutes();
    }
}
