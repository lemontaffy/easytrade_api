package com.cake.easytrade.service;

import com.cake.easytrade.mapper.DetailItemMapper;
import com.cake.easytrade.mapper.ItemMapper;
import com.cake.easytrade.model.DeliveryFee;
import com.cake.easytrade.model.DetailItem;
import com.cake.easytrade.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private DetailItemMapper detailItemMapper;

    public void createItemWithDetails(Item item, List<DeliveryFee> deliveryFees, List<DetailItem> detailItems) throws Exception {
        // Validate that at least one detail item is provided
        if (detailItems == null || detailItems.isEmpty()) {
            throw new IllegalArgumentException("At least one detail item is required.");
        }

        // Convert deliveryFees to JSON and set in the item
        item.setDeliveryFeeAsList(deliveryFees);

        // Insert the main item
        itemMapper.insertItem(item);

        // Insert detail items
        for (DetailItem detailItem : detailItems) {
            detailItem.setItemId(item.getId()); // Set the generated item ID
            detailItemMapper.insertDetailItem(detailItem);
        }
    }
}
