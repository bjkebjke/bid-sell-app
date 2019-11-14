package com.buysell.demo.util;

import com.buysell.demo.model.Bid;
import com.buysell.demo.model.Item;
import com.buysell.demo.model.User;
import com.buysell.demo.payload.ItemResponse;
import com.buysell.demo.payload.UserSummary;
import com.buysell.demo.repository.BidRepository;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.stream.Collectors;

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

        itemResponse.setImageIds(item.getImageIds());
        itemResponse.setBase64Images(item.getImages().stream().map(image -> Base64.encode(image.getPicture())).collect(Collectors.toList()));

        return itemResponse;
    }
}
