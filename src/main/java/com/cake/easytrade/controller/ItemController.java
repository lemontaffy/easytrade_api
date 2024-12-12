package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.model.*;
import com.cake.easytrade.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "ItemController", description = "itemController")
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Const.API)
public class ItemController extends BaseController {

    private static final String path = "/items";

    private ItemService itemService;

    @PostMapping(path = path, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createItemWithDetails(
            @RequestPart("item") String itemJson,
            @RequestPart("detailItems") List<String> detailItemsJson,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "detailItemThumbnails", required = false) List<MultipartFile> detailItemThumbnails) {
        try {
            // Parse JSON strings to objects
            ObjectMapper objectMapper = new ObjectMapper();
            Item item = objectMapper.readValue(itemJson, Item.class);

            List<DetailItem> detailItems = detailItemsJson.stream()
                    .map(json -> {
                        try {
                            return objectMapper.readValue(json, DetailItem.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Error parsing detailItems JSON: " + e.getMessage(), e);
                        }
                    })
                    .collect(Collectors.toList());

            // Pass data to the service layer
            itemService.createItemWithDetails(item, detailItems, thumbnail, detailItemThumbnails);

            return ResponseEntity.ok("Item and details created successfully.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the request: " + e.getMessage());
        }
    }
}
