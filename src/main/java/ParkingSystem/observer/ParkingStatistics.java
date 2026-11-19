package ParkingSystem.observer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ParkingStatistics {

    private final AtomicInteger totalParked = new AtomicInteger();
    private final AtomicInteger totalExited = new AtomicInteger();

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVehicleParked(VehicleParkedEvent event) {
        totalParked.incrementAndGet();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVehicleExited(VehicleExitedEvent event) {
        totalExited.incrementAndGet();
    }

    public int getTotalParked() { return totalParked.get(); }

    public int getTotalExited() { return totalExited.get(); }
}