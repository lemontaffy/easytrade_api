package com.cake.easytrade.mapper;

import com.cake.easytrade.model.DetailItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface DetailItemMapper {

    @Insert("""
        INSERT INTO detail_items (
            item_id, detail_name, detail_description, detail_image_url, price, leftovers, max_buy_count
        ) VALUES (
            #{itemId}, #{detailName}, #{detailDescription}, #{detailImageUrl}, #{price}, #{leftovers}, #{maxBuyCount}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDetailItem(DetailItem detailItem);
}
