package com.tujuhsembilan.resturant_reservation.service;

import com.tujuhsembilan.resturant_reservation.dto.ReservationDTO;
import com.tujuhsembilan.resturant_reservation.model.Reservation;
import com.tujuhsembilan.resturant_reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // Hari libur: Rabu dan Jumat
    private boolean isHoliday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.WEDNESDAY || day == DayOfWeek.FRIDAY;
    }

    // Maksimal 2 customer per hari
    private boolean isFull(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findAllByReservationDate(date);
        return reservations.size() >= 2;
    }

    // Menambah reservasi baru
    public Reservation addReservation(ReservationDTO reservationDTO) {
        LocalDate date = reservationDTO.getReservationDate();

        if (isHoliday(date) || isFull(date)) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setCustomerName(reservationDTO.getCustomerName());
        reservation.setReservationDate(date);
        reservation.setStatus("Confirmed");

        return reservationRepository.save(reservation); // Kembalikan entitas yang disimpan
    }

    // Mendapatkan reservasi berdasarkan ID
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    // Menampilkan antrian selama 1 minggu dari tanggal tertentu
    public List<Reservation> getWeeklyReservations(LocalDate startDate) {
        return IntStream.range(0, 7)
                .mapToObj(startDate::plusDays)
                .filter(date -> !isHoliday(date)) // Hanya tanggal non-libur
                .flatMap(date -> reservationRepository.findAllByReservationDate(date).stream())
                .collect(Collectors.toList());
    }

    // Mendapatkan antrian reservasi berdasarkan custom startDate dan jumlah hari
    public List<Reservation> getCustomWeekReservations(LocalDate startDate, int days) {
        return IntStream.range(0, days)
                .mapToObj(startDate::plusDays)
                .filter(date -> !isHoliday(date)) // Hanya tanggal non-libur
                .flatMap(date -> reservationRepository.findAllByReservationDate(date).stream())
                .collect(Collectors.toList());
    }
}

