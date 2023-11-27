package com.prithvi.youtility.repository;

import com.prithvi.youtility.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByStatus(String status);

    List<Appointment> findByStatusAndPostedBy(String status, String postedBy);
}
