package com.tujuhsembilan.resturant_reservation.repository;

import com.tujuhsembilan.resturant_reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByReservationDate(LocalDate reservationDate);
}
