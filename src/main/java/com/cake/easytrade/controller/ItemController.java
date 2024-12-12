package com.cake.easytrade.controller;

import com.cake.easytrade.core.Const;
import com.cake.easytrade.model.DeliveryFee;
import com.cake.easytrade.model.DetailItem;
import com.cake.easytrade.model.Item;
import com.cake.easytrade.model.ItemWithDetailsDTO;
import com.cake.easytrade.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ItemController", description = "itemController")
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = Const.API)
public class ItemController extends BaseController {

    private static final String path = "/item";

    private ItemService itemService;

    @PostMapping(path = path + "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = Const.REST_CONTENT_TYPE)
    public ResponseEntity<?> createItemWithDetails(@RequestBody ItemWithDetailsDTO request) {
        try {
            Item item = request.getItem();
            List<DeliveryFee> deliveryFees = request.getDeliveryFees();
            List<DetailItem> detailItems = request.getDetailItems();

            itemService.createItemWithDetails(item, deliveryFees, detailItems);

            return ResponseEntity.ok("Item and details created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
