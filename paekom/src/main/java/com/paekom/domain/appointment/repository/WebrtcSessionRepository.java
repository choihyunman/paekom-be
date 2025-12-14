package com.paekom.domain.appointment.repository;

import com.paekom.domain.appointment.entity.Appointment;
import com.paekom.domain.appointment.entity.WebrtcSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebrtcSessionRepository extends JpaRepository<WebrtcSession, Integer> {
    WebrtcSession findByAppointment(Appointment appointment);
}
