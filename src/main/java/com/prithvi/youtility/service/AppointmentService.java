package com.prithvi.youtility.service;

import com.prithvi.youtility.entity.Appointment;
import com.prithvi.youtility.model.ResponseModel;
import com.prithvi.youtility.repository.AppointmentRepository;
import com.prithvi.youtility.repository.UserRepository;
import com.prithvi.youtility.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.prithvi.youtility.config.Constants.STATUS_ACTIVE;
import static java.util.Objects.nonNull;


@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JavaMailSender emailSender;

    public List<Appointment> fetchProducts() {
        return appointmentRepository.findByStatus(STATUS_ACTIVE);
    }

    public List<Appointment> fetchMyProducts(String userId) {
        return appointmentRepository.findByStatusAndPostedBy(STATUS_ACTIVE, userId);
    }

    @Value("${s3ImageUrl}")
    private String s3ImageUrl;

    public ResponseModel saveProduct(Appointment property, String userName) {
        ResponseModel response = new ResponseModel();
        try {
            property.setId(UUID.randomUUID());
            property.setStatus("A");
            property.setPostedOn(LocalDateTime.now());
            property.setPostedBy(userName);
            appointmentRepository.save(property);
            response.setStatus("Success");
            response.setMessage("Operation Successful");
            response.setCreationId(property.getId().toString());
        } catch (Exception e) {
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    public ResponseModel updateProperty(UUID propertyId, Appointment property, String userName) {
        ResponseModel response = new ResponseModel();
        try {
            appointmentRepository.findById(propertyId).ifPresent(
                    savedProperty -> {
                        if (savedProperty.getPostedBy().equals(userName)) {
                            setUpdatedValues(savedProperty, property);
                            appointmentRepository.save(savedProperty);
                            response.setStatus("Success");
                            response.setMessage("Operation Successful");
                        } else {
                            response.setStatus("Unsuccessful");
                            response.setMessage("Operation Unsuccessful : Not Authorized To Update This Property");
                        }
                    }
            );
        } catch (Exception e) {
            response.setStatus("Unsuccessful");
            response.setMessage("Exception Occurred");
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    private void setUpdatedValues(Appointment property, Appointment updatedProp) {
        if (nonNull(updatedProp.getAddress()))
            property.setAddress(updatedProp.getAddress());

        if (nonNull(updatedProp.getYearMade()))
            property.setYearMade(updatedProp.getYearMade());

        if (nonNull(updatedProp.getPropertyType()))
            property.setPropertyType(updatedProp.getPropertyType());

        if (nonNull(updatedProp.getDescription()))
            property.setDescription(updatedProp.getDescription());

        property.setModifiedOn(LocalDateTime.now());
    }

    public ResponseModel inactiveProperty(UUID propertyId, String userName) {
        ResponseModel response = new ResponseModel();
        try {
            appointmentRepository.findById(propertyId).ifPresent(
                    savedProperty -> {
                        if (savedProperty.getPostedBy().equals(userName)) {
                            savedProperty.setStatus("I");
                            appointmentRepository.save(savedProperty);
                            response.setStatus("Success");
                            response.setMessage("Operation Successful");
                        } else {
                            response.setStatus("Unsuccessful");
                            response.setMessage("Operation Unsuccessful : Not Authorized To Update This Property");
                        }
                    }
            );
        } catch (Exception e) {
            response.setStatus("Unsuccessful");
            response.setMessage("Exception Occurred");
            response.setException(e.getLocalizedMessage());
        }
        return response;
    }

    public ResponseModel emailPropertyOwner(UUID productId, String userName) {
        ResponseModel response = new ResponseModel();
        Appointment prod = appointmentRepository.findById(productId).orElse(null);
        User productUser = null;
        if (nonNull(prod)) {
            productUser = userRepository.findByUsername(prod.getPostedBy()).orElse(null);
        }
        User enquiryUser = userRepository.findByUsername(userName).orElse(null);
        if (nonNull(productUser) && nonNull(enquiryUser)) {
            sendEmail(productUser, enquiryUser, prod);
        }
        response.setStatus("Success");
        response.setMessage("Operation Successful");
        return response;
    }

    private void sendEmail(User productUser, User enquiryUser, Appointment property) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("honeyspace.app@gmail.com");
        message.setTo(productUser.getEmail());
        message.setSubject("Enquiry for your services : " + property.getPropertyType());
        message.setText("A person has made an enquiry for your property : "
                + property.getPostedBy() +
                ", kindly Email the potential buyer of your property. Here Is The Details Of Enquirer. " +
                " Name: " + enquiryUser.getFirstname() + " " + enquiryUser.getLastname() + " " +
                " Email ID: " + enquiryUser.getEmail());
        this.emailSender.send(message);
    }
}
