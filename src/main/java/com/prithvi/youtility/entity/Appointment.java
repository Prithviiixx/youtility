package com.prithvi.youtility.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
public class Appointment {
    @Id
    private UUID id;
    private String address;
    private LocalDate yearMade;
    private String description;
    private String propertyType;
    private String status;
    private String postedBy;
    private LocalDateTime postedOn;
    private LocalDateTime modifiedOn;
}
