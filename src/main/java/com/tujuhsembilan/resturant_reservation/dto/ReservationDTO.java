package com.tujuhsembilan.resturant_reservation.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationDTO {
    private String customerName;
    private LocalDate reservationDate;
}
