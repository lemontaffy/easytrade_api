package com.cake.easytrade.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailItem {
    private long id;
    private long itemId;
    private String detailName;
    private String detailImgUrl;
    private Integer price;
    private Integer leftovers;
    private Integer maxBuyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
