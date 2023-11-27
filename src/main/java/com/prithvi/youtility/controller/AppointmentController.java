package com.prithvi.youtility.controller;

import com.prithvi.youtility.entity.Appointment;
import com.prithvi.youtility.model.ResponseModel;
import com.prithvi.youtility.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/property")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getProperties(Principal principal) {
        return new ResponseEntity<>(appointmentService.fetchProducts(), HttpStatus.OK);
    }

    @GetMapping("/myProperties")
    public ResponseEntity<List<Appointment>> getListedProperties(Principal principal) {
        return new ResponseEntity<>(appointmentService.fetchMyProducts(principal.getName()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseModel> postProperty(@RequestBody Appointment product, Principal principal) {
        return new ResponseEntity<>(appointmentService.saveProduct(product, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/{propertyId}")
    public ResponseEntity<ResponseModel> updateProperty(@PathVariable UUID propertyId,
                                                        @RequestBody Appointment property,
                                                        Principal principal) {
        return new ResponseEntity<>(appointmentService.updateProperty(propertyId, property, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<ResponseModel> deleteProperty(@PathVariable UUID propertyId, Principal principal) {
        return new ResponseEntity<>(appointmentService.inactiveProperty(propertyId, principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/contact/{propertyId}")
    public ResponseEntity<ResponseModel> contactOwner(@PathVariable UUID propertyId, Principal principal) {
        return new ResponseEntity<>(appointmentService.emailPropertyOwner(propertyId, principal.getName()), HttpStatus.CREATED);
    }
}
