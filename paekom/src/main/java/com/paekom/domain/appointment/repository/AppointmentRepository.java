package com.paekom.domain.appointment.repository;

import com.paekom.domain.appointment.dto.AppointmentResponse;
import com.paekom.domain.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("""
        select new com.paekom.domain.appointment.dto.AppointmentResponse(
            a.id
            , a.scheduledDate
            , a.scheduledTime
            , a.status
        )
        from Appointment a
        order by a.scheduledDate, a.scheduledTime
    """)
    List<AppointmentResponse> findAllAppointments();
}
