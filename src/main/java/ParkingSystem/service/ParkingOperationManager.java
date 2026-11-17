package ParkingSystem.service;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.locks.ReentrantLock;

public final class ParkingOperationManager {

    private static final ParkingOperationManager INSTANCE =
            new ParkingOperationManager();

    private final ReentrantLock operationLock =
            new ReentrantLock(true);

    private ParkingOperationManager() {
    }

    public static ParkingOperationManager getInstance() {
        return INSTANCE;
    }

    public void lockUntilTransactionCompletes() {

        operationLock.lock();

        boolean registered = false;

        try {

            if (!TransactionSynchronizationManager
                    .isSynchronizationActive()) {

                throw new IllegalStateException(
                        "Няма активна Spring транзакция."
                );
            }

            TransactionSynchronizationManager
                    .registerSynchronization(
                            new TransactionSynchronization() {

                                @Override
                                public void afterCompletion(int status) {
                                    operationLock.unlock();
                                }
                            }
                    );

            registered = true;

        } finally {

            if (!registered) {
                operationLock.unlock();
            }
        }
    }
}
