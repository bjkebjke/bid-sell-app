package com.buysell.demo.util;

import com.buysell.demo.model.Bid;
import com.buysell.demo.model.Item;
import com.buysell.demo.model.User;
import com.buysell.demo.payload.ItemResponse;
import com.buysell.demo.payload.UserSummary;

import java.time.Instant;

public class ModelMapper {

    public static ItemResponse mapItemToItemResponse(Item item, User creator) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setItemName(item.getItemName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setCreationDateTime(item.getCreatedAt());
        itemResponse.setExpirationDateTime(item.getExpirationDateTime());

        //new
        itemResponse.setBids(item.getBids());

        Instant now = Instant.now();
        itemResponse.setExpired(item.getExpirationDateTime().isBefore(now));

        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        itemResponse.setCreatedBy(creatorSummary);

        //delete later?
        itemResponse.setTopBid(item.getTopBid());

        return itemResponse;
    }
}
