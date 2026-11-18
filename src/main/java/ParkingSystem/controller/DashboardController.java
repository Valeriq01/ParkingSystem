package ParkingSystem.controller;

import ParkingSystem.exception.ParkingException;
import ParkingSystem.model.enums.VehicleType;
import ParkingSystem.composite.ParkingLot;
import ParkingSystem.service.ParkingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@Validated
public class DashboardController {

    private final ParkingService parkingService;

    public DashboardController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        ParkingLot lot = parkingService.getParkingLot();

        model.addAttribute("totalSpots", lot.getTotalSpots());
        model.addAttribute("freeSpots", lot.getFreeSpots());
        model.addAttribute("occupiedSpots", lot.getOccupiedSpots());

        model.addAttribute("activeSessions",
                parkingService.getActiveSessions());

        model.addAttribute("parkingLevels",
                parkingService.getParkingLayout());

        model.addAttribute("vehicleTypes",
                Arrays.asList(VehicleType.values()));

        return "dashboard";
    }

    @PostMapping("/park")
    public String park(
            @RequestParam String licensePlate,
            @RequestParam("vehicleType") VehicleType type,
            RedirectAttributes redirect
    ) {
        try {
            parkingService.park(licensePlate, type);

            redirect.addFlashAttribute(
                    "success",
                    "Превозното средство е паркирано успешно."
            );

        } catch (ParkingException | IllegalArgumentException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/release/{sessionId}")
    public String release(
            @PathVariable Long sessionId,
            RedirectAttributes redirect
    ) {
        try {
            parkingService.release(sessionId);

            redirect.addFlashAttribute(
                    "success",
                    "Паркоместата са освободени успешно."
            );

        } catch (ParkingException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/";
    }

    @GetMapping("/history")
    public String history(Model model) {

        model.addAttribute(
                "sessions",
                parkingService.getHistory()
        );

        return "history";
    }
}
