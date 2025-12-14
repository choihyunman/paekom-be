package com.paekom.domain.appointment.dto;

import com.paekom.domain.appointment.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class AppointmentResponse {
    private Integer id;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private AppointmentStatus status;
}
