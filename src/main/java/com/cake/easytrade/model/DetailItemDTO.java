package com.cake.easytrade.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailItemDTO {
    private String detailName;
    private MultipartFile detailImageFile;
    private Integer price;
    private Integer leftovers;
    private Integer maxBuyCount;
}