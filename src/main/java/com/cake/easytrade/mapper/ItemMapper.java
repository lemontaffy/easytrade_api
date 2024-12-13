package com.cake.easytrade.mapper;

import com.cake.easytrade.model.Item;
import com.cake.easytrade.model.SimpleItemDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Insert("""
        INSERT INTO items (
            user_id, profile_id, thumbnail_url, title, tags, sales_start_date,
            sales_end_date, qr_code, delivery_fee, product_board
        ) VALUES (
            #{userId}, #{profileId}, #{thumbnailUrl}, #{title}, #{tags}::jsonb, #{salesStartDate},
            #{salesEndDate}, #{qrCode}, #{deliveryFee}::jsonb, #{productBoard}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertItem(Item item);

    @Select("SELECT id, thumbnail_url as thumbnailUrl, title, likes, hits FROM items")
    List<SimpleItemDTO> findAllItems();
}
