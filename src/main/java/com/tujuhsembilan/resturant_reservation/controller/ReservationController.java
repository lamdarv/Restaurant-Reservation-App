package com.tujuhsembilan.resturant_reservation.controller;

import com.tujuhsembilan.resturant_reservation.dto.ReservationDTO;
import com.tujuhsembilan.resturant_reservation.model.Reservation;
import com.tujuhsembilan.resturant_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Add Reservation
    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation savedReservation = reservationService.addReservation(reservationDTO);

        if (savedReservation == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Reservation failed due to full capacity or holiday");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", savedReservation);
        response.put("message", "Reservation with ID " + savedReservation.getId() + " successfully added");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);

        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reservation with ID " + id + " not found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", reservation);
        response.put("message", "Reservation with ID " + id + " found");

        return ResponseEntity.ok(response);
    }

    // Endpoint untuk menampilkan antrian selama 1 minggu dari hari ini
    @GetMapping("/week")
    public ResponseEntity<?> getWeeklyReservations() {
        LocalDate today = LocalDate.now();
        List<Reservation> reservations = reservationService.getWeeklyReservations(today);

        Map<String, Object> response = new HashMap<>();
        response.put("data", reservations);
        response.put("message", "Reservations for the week starting from " + today);

        return ResponseEntity.ok(response);
    }

    // Endpoint untuk menampilkan antrian dengan custom startDate dan jumlah hari
    @GetMapping("/custom-week")
    public ResponseEntity<?> getCustomWeekReservations(
            @RequestParam("startDate") String startDateStr,
            @RequestParam(value = "days", defaultValue = "7") int days) {
        try {
            // Konversi string ke LocalDate
            LocalDate startDate = LocalDate.parse(startDateStr);
            List<Reservation> reservations = reservationService.getCustomWeekReservations(startDate, days);

            Map<String, Object> response = new HashMap<>();
            response.put("data", reservations);
            response.put("message", "Reservations from " + startDate + " for " + days + " days");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}

