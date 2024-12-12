package com.cake.easytrade.mapper;

import com.cake.easytrade.model.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ItemMapper {

    @Insert("""
        INSERT INTO items (
            user_id, profile_id, thumbnail_url, title, tags, sales_start_date,
            sales_end_date, qr_code, delivery_fee, leftovers, product_board
        ) VALUES (
            #{userId}, #{profileId}, #{thumbnailUrl}, #{title}, #{tags}::jsonb, #{salesStartDate},
            #{salesEndDate}, #{qrCode}, #{deliveryFee}::jsonb, #{leftovers}, #{productBoard}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Item insertItem(Item item);
}
