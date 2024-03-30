package com.Fujitsu.Fooddelivery.model;

public class DeliveryFeeResponse {
    private double deliveryFee;

    // Constructors
    public DeliveryFeeResponse() {
    }

    public DeliveryFeeResponse(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    // Getters and Setters
    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}

