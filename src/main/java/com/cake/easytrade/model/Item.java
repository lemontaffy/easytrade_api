package com.cake.easytrade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private Long id;
    private Long userId;
    private Long profileId;
    private String thumbnailUrl;
    private String title;
    private String tags; // Stored as JSONB
    private LocalDateTime salesStartDate;
    private LocalDateTime salesEndDate;
    private String qrCode;
    private String deliveryFee; // Stored as JSONB
    private Integer leftovers;
    private String productBoard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Utility to parse deliveryFee into a List
    @JsonIgnoreProperties
    public List<DeliveryFee> getDeliveryFeeAsList() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(deliveryFee, new TypeReference<List<DeliveryFee>>() {});
    }

    // Utility to convert List to JSON
    public void setDeliveryFeeAsList(List<DeliveryFee> deliveryFees) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.deliveryFee = objectMapper.writeValueAsString(deliveryFees);
    }

}
