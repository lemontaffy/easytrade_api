package com.cake.easytrade.service;

import com.cake.easytrade.mapper.DetailItemMapper;
import com.cake.easytrade.mapper.ItemMapper;
import com.cake.easytrade.model.DetailItem;
import com.cake.easytrade.model.Item;
import com.cake.easytrade.model.SimpleItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private DetailItemMapper detailItemMapper;

    @Autowired
    private FileStorageService fileStorageService; // A service to handle file storage

    public void createItemWithDetails(Item item, List<DetailItem> detailItems, MultipartFile thumbnail, List<MultipartFile> detailItemThumbnails) {
        try {
            // Save the main item
            if (thumbnail != null) {
                String thumbnailPath = fileStorageService.storeFile(thumbnail, "items/thumbnails");
                item.setThumbnailUrl("uploads" + thumbnailPath);
            }

            if (item.getTags() == null || item.getTags().isEmpty()) {
                item.setTags("{}"); // Set to empty JSON object
            }
            if (item.getDeliveryFee() == null || item.getDeliveryFee().isEmpty()) {
                item.setDeliveryFee("{}"); // Set to empty JSON object
            }

            itemMapper.insertItem(item);

            // Save each detail item
            for (int i = 0; i < detailItems.size(); i++) {
                DetailItem detailItem = detailItems.get(i);

                // Ensure thumbnails list alignment
                if (detailItemThumbnails != null && i < detailItemThumbnails.size()) {
                    MultipartFile detailThumbnail = detailItemThumbnails.get(i);
                    if (detailThumbnail != null && !detailThumbnail.isEmpty()) {
                        String detailThumbnailPath = fileStorageService.storeFile(detailThumbnail, "items/details");
                        detailItem.setDetailImgUrl("uploads" + detailThumbnailPath);
                    }
                }

                detailItem.setItemId(item.getId()); // Link to the parent item
                detailItemMapper.insertDetailItem(detailItem);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create item with details: " + e.getMessage(), e);
        }
    }

    public List<SimpleItemDTO> getAllItems() {
        return itemMapper.findAllItems();
    }
}
