package ParkingSystem.service;

import ParkingSystem.model.entity.ParkingSpot;
import ParkingSystem.repository.ParkingSpotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ParkingSpotRepository spotRepository;

    public DataInitializer(ParkingSpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    @Override
    public void run(String... args) {

        if (spotRepository.count() == 0) {

            for (int level = 1; level <= 2; level++) {
                for (int row = 1; row <= 3; row++) {
                    for (int spot = 1; spot <= 8; spot++) {

                        spotRepository.save(
                                new ParkingSpot(level, row, spot)
                        );
                    }
                }
            }
        }
    }
}

