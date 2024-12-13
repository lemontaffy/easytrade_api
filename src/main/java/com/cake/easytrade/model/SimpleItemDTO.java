package com.cake.easytrade.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleItemDTO {
    private Long id;
    private String thumbnailUrl;
    private String title;
    private Integer likes;
    private Integer hits;
}
