package com.buysell.demo.util;

import com.buysell.demo.entity.Bid;
import com.buysell.demo.entity.Item;
import com.buysell.demo.entity.User;
import com.buysell.demo.payload.ItemResponse;
import com.buysell.demo.payload.UserSummary;

import java.time.Instant;

public class ModelMapper {

    public static ItemResponse mapItemToItemResponse(Item item, User creator, Bid userBid) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(item.getId());
        itemResponse.setItemName(item.getItemName());
        itemResponse.setDescription(item.getDescription());
        itemResponse.setCreationDateTime(item.getCreatedAt());
        itemResponse.setExpirationDateTime(item.getExpirationDateTime());

        Instant now = Instant.now();
        itemResponse.setExpired(item.getExpirationDateTime().isBefore(now));

        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        itemResponse.setCreatedBy(creatorSummary);

        if(userBid != null) {
            itemResponse.setNewBid(userBid);
        }

        itemResponse.setTopBid(item.getTopBid());

        return itemResponse;
    }
}
