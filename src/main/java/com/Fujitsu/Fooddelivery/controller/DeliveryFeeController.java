package com.Fujitsu.Fooddelivery.controller;

import com.Fujitsu.Fooddelivery.service.DeliveryFeeCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryFeeController {

    private final DeliveryFeeCalculatorService deliveryFeeCalculatorService;

    @Autowired
    public DeliveryFeeController(DeliveryFeeCalculatorService deliveryFeeCalculatorService) {
        this.deliveryFeeCalculatorService = deliveryFeeCalculatorService;
    }

    @GetMapping("/delivery-fee")
    public ResponseEntity<Double> calculateDeliveryFee(
            @RequestParam String city,
            @RequestParam String vehicleType
    ) {
        try {
            double deliveryFee = deliveryFeeCalculatorService.calculateDeliveryFee(city, vehicleType);
            return ResponseEntity.ok(deliveryFee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Handle invalid input parameters
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Handle other exceptions
        }
    }
}

