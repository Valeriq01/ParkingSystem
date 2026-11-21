package ParkingSystem.observer;

import ParkingSystem.observer.VehicleExitedEvent;
import ParkingSystem.observer.VehicleParkedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ParkingEventListener {

    private static final Logger log =
            LoggerFactory.getLogger(ParkingEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVehicleParked(VehicleParkedEvent event) {
        log.info("PARKED: {} | session={} | spots={}",
                event.licensePlate(), event.sessionId(), event.allocatedSpots());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVehicleExited(VehicleExitedEvent event) {
        log.info("EXITED: {} | session={}",
                event.licensePlate(), event.sessionId());
    }
}