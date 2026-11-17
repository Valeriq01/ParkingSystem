package ParkingSystem.service;

import ParkingSystem.composite.ParkingLevel;
import ParkingSystem.composite.ParkingRow;
import ParkingSystem.composite.ParkingLot;
import ParkingSystem.exception.ParkingException;
import ParkingSystem.factory.VehicleFactory;
import ParkingSystem.model.entity.ParkingSession;
import ParkingSystem.model.entity.ParkingSpot;
import ParkingSystem.model.entity.Vehicle;
import ParkingSystem.model.enums.SpotStatus;
import ParkingSystem.model.enums.VehicleType;
import ParkingSystem.model.view.ParkingCellView;
import ParkingSystem.model.view.ParkingLevelView;
import ParkingSystem.model.view.ParkingRowView;
import ParkingSystem.validation.LicensePlate;
import ParkingSystem.observer.VehicleExitedEvent;
import ParkingSystem.observer.VehicleParkedEvent;
import ParkingSystem.repository.ParkingSessionRepository;
import ParkingSystem.repository.ParkingSpotRepository;
import ParkingSystem.service.ParkingOperationManager;
import ParkingSystem.repository.VehicleRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final ParkingSpotRepository spotRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSessionRepository sessionRepository;
    private final VehicleFactory vehicleFactory;
    private final ApplicationEventPublisher events;

    public ParkingService(
            ParkingSpotRepository spotRepository,
            VehicleRepository vehicleRepository,
            ParkingSessionRepository sessionRepository,
            VehicleFactory vehicleFactory,
            ApplicationEventPublisher events
    ) {
        this.spotRepository = spotRepository;
        this.vehicleRepository = vehicleRepository;
        this.sessionRepository = sessionRepository;
        this.vehicleFactory = vehicleFactory;
        this.events = events;
    }

    @Transactional
    public ParkingSession park(String plate, VehicleType type) {

        ParkingOperationManager.getInstance()
                .lockUntilTransactionCompletes();

        String normalizedPlate = LicensePlate.normalize(plate);

        if (normalizedPlate.isBlank()) {
            throw new ParkingException(
                    "Въведете регистрационен номер."
            );
        }

        if (type == null) {
            throw new ParkingException(
                    "Изберете тип превозно средство."
            );
        }

        if (!LicensePlate.isValid(normalizedPlate)) {
            throw new ParkingException(
                    "Невалиден регистрационен номер. Пример: CA1234AB"
            );
        }

        boolean alreadyParked =
                sessionRepository
                        .findByExitTimeIsNullOrderByEntryTimeDesc()
                        .stream()
                        .anyMatch(s -> s.getVehicle()
                                .getLicensePlate()
                                .equalsIgnoreCase(normalizedPlate));

        if (alreadyParked) {
            throw new ParkingException(
                    "Това превозно средство вече е в паркинга."
            );
        }

        List<ParkingSpot> all =
                spotRepository
                        .findAllByOrderByLevelNumberAscRowNumberAscSpotNumberAsc();

        List<ParkingSpot> allocation =
                findContiguousSpots(all, type.getRequiredSpots());

        if (allocation.isEmpty()) {
            throw new ParkingException(
                    "Няма достатъчно последователни свободни места."
            );
        }

        Vehicle vehicle = vehicleRepository
                .findByLicensePlateIgnoreCase(normalizedPlate)
                .map(existingVehicle -> {

                    if (existingVehicle.getType() != type) {
                        throw new ParkingException(
                                "Този регистрационен номер вече е регистриран с друг тип превозно средство."
                        );
                    }

                    return existingVehicle;
                })
                .orElseGet(() -> vehicleRepository.save(
                        vehicleFactory.create(normalizedPlate, type)
                ));

        ParkingSession session =
                sessionRepository.save(new ParkingSession(vehicle));

        allocation.forEach(spot -> spot.occupy(session));

        spotRepository.saveAll(allocation);

        events.publishEvent(
                new VehicleParkedEvent(
                        session.getId(),
                        vehicle.getLicensePlate(),
                        allocation.size()
                )
        );

        return session;
    }

    private List<ParkingSpot> findContiguousSpots(
            List<ParkingSpot> all,
            int required
    ) {

        Map<String, List<ParkingSpot>> rows =
                all.stream().collect(Collectors.groupingBy(
                        s -> s.getLevelNumber() + "-" + s.getRowNumber(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        for (List<ParkingSpot> row : rows.values()) {

            row.sort(
                    Comparator.comparingInt(ParkingSpot::getSpotNumber)
            );

            List<ParkingSpot> run = new ArrayList<>();

            int previousSpotNumber = -1;

            for (ParkingSpot spot : row) {

                if (spot.getStatus() == SpotStatus.FREE) {

                    if (!run.isEmpty()
                            && spot.getSpotNumber() != previousSpotNumber + 1) {

                        run.clear();
                    }

                    run.add(spot);

                    if (run.size() == required) {
                        return new ArrayList<>(run);
                    }

                    previousSpotNumber = spot.getSpotNumber();

                } else {

                    run.clear();
                    previousSpotNumber = -1;
                }
            }
        }

        return List.of();
    }

    @Transactional
    public void release(Long sessionId) {

        ParkingOperationManager.getInstance()
                .lockUntilTransactionCompletes();

        ParkingSession session =
                sessionRepository
                        .findByIdAndExitTimeIsNull(sessionId)
                        .orElseThrow(() -> new ParkingException(
                                "Активното паркиране не е намерено."
                        ));

        List<ParkingSpot> occupied =
                spotRepository.findBySession_Id(sessionId);

        occupied.forEach(ParkingSpot::release);

        spotRepository.saveAll(occupied);

        session.close();

        sessionRepository.save(session);

        events.publishEvent(
                new VehicleExitedEvent(
                        sessionId,
                        session.getVehicle().getLicensePlate()
                )
        );
    }

    @Transactional(readOnly = true)
    public List<ParkingSpot> getAllSpots() {
        return spotRepository
                .findAllByOrderByLevelNumberAscRowNumberAscSpotNumberAsc();
    }

    @Transactional(readOnly = true)
    public List<ParkingSession> getActiveSessions() {
        return sessionRepository
                .findByExitTimeIsNullOrderByEntryTimeDesc();
    }

    @Transactional(readOnly = true)
    public List<ParkingSession> getHistory() {
        return sessionRepository.findAllByOrderByEntryTimeDesc();
    }

    @Transactional(readOnly = true)
    public ParkingLot getParkingLot() {

        ParkingLot lot = new ParkingLot();
        Map<Integer, ParkingLevel> levels = new LinkedHashMap<>();
        Map<String, ParkingRow> rows = new HashMap<>();

        for (ParkingSpot spot : getAllSpots()) {

            ParkingLevel level = levels.computeIfAbsent(
                    spot.getLevelNumber(),
                    number -> {
                        ParkingLevel created = new ParkingLevel(number);
                        lot.add(created);
                        return created;
                    }
            );

            String rowKey = spot.getLevelNumber() + "-" + spot.getRowNumber();

            ParkingRow row = rows.computeIfAbsent(
                    rowKey,
                    key -> {
                        ParkingRow created = new ParkingRow(spot.getRowNumber());
                        level.add(created);
                        return created;
                    }
            );

            row.add(spot);
        }

        return lot;
    }

    @Transactional(readOnly = true)
    public List<ParkingLevelView> getParkingLayout() {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        List<ParkingLevelView> result = new ArrayList<>();

        for (ParkingLevel level : getParkingLot().getLevels()) {

            List<ParkingRowView> rowViews = new ArrayList<>();

            for (ParkingRow row : level.getRows()) {

                List<ParkingCellView> cells = new ArrayList<>();
                Set<Long> displayedSessions = new HashSet<>();

                for (ParkingSpot spot : row.getSpots()) {

                    if (spot.getStatus() == SpotStatus.FREE) {
                        cells.add(new ParkingCellView(spot.getSpotNumber()));
                        continue;
                    }

                    ParkingSession session = spot.getSession();

                    if (session == null
                            || !displayedSessions.add(session.getId())) {
                        continue;
                    }

                    Vehicle vehicle = session.getVehicle();
                    VehicleType type = vehicle.getType();

                    cells.add(
                            new ParkingCellView(
                                    spot.getSpotNumber(),
                                    session.getId(),
                                    vehicle.getLicensePlate(),
                                    type.name().toLowerCase(),
                                    type.getLabel(),
                                    session.getEntryTime().format(formatter),
                                    type.getRequiredSpots()
                            )
                    );
                }

                rowViews.add(
                        new ParkingRowView(
                                row.getRowNumber(),
                                row.getTotalSpots(),
                                row.getFreeSpots(),
                                cells
                        )
                );
            }

            result.add(
                    new ParkingLevelView(
                            level.getLevelNumber(),
                            level.getTotalSpots(),
                            level.getFreeSpots(),
                            rowViews
                    )
            );
        }

        return result;
    }
}
