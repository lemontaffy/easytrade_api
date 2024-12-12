package com.cake.easytrade.model;

import lombok.Data;

@Data
public class DeliveryFee {
    private String method; // e.g., "postOffice"
    private Double fee;    // e.g., 4000.0
}
