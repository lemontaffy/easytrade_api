package com.cake.easytrade.model;

import lombok.Data;
import java.util.List;

@Data
public class ItemWithDetailsDTO {
    private Item item;
    private List<DetailItem> detailItems;
    private List<DeliveryFee> deliveryFees;
}
