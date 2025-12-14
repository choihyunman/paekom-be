package com.paekom.domain.appointment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class AppointmentRequest {
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
}
