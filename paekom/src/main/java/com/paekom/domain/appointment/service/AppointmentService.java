package com.paekom.domain.appointment.service;

import com.paekom.domain.appointment.dto.AppointmentRequest;
import com.paekom.domain.appointment.dto.AppointmentResponse;
import com.paekom.domain.appointment.entity.Appointment;
import com.paekom.domain.appointment.entity.AppointmentStatus;
import com.paekom.domain.appointment.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    // 레포지토리
    private final AppointmentRepository repository;

    // 상담 예약 단건 조회
    public Appointment getAppointmentById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // 상담 예약 생성
    public void createAppointment(AppointmentRequest request){
        Appointment appointment = Appointment.builder()
                .scheduledDate(request.getScheduledDate())
                .scheduledTime(request.getScheduledTime())
                .build();

        repository.save(appointment);
    }

    // 상담 예약 조회
    public List<AppointmentResponse> getAppointments(){
        return repository.findAllAppointments();
    }

    // 상담 예약 취소
    public void cancelAppointment(Integer id){
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        repository.save(appointment);
    }

    // 상담 시작
    public Appointment startedAppointment(Integer id){
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.STARTED);
        repository.save(appointment);

        return appointment;
    }

    // 상담 종료
    public Appointment completedAppointment(Integer id){
        Appointment appointment = getAppointmentById(id);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        repository.save(appointment);

        return appointment;
    }

}
