package com.buysell.demo.service;

import com.buysell.demo.model.Bid;
import com.buysell.demo.model.Item;
import com.buysell.demo.model.User;
import com.buysell.demo.exception.BadRequestException;
import com.buysell.demo.exception.ResourceNotFoundException;
import com.buysell.demo.payload.BidRequest;
import com.buysell.demo.payload.ItemRequest;
import com.buysell.demo.payload.ItemResponse;
import com.buysell.demo.payload.PagedResponse;
import com.buysell.demo.repository.BidRepository;
import com.buysell.demo.repository.ItemRepository;
import com.buysell.demo.repository.UserRepository;
import com.buysell.demo.security.UserPrincipal;
import com.buysell.demo.util.AppConstants;
import com.buysell.demo.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    public PagedResponse<ItemResponse> getAllItems(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Items
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Item> items = itemRepository.findAll(pageable);

        if(items.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), items.getNumber(),
                    items.getSize(), items.getTotalElements(), items.getTotalPages(), items.isLast());
        }

        // Map Items to ItemResponses
        List<Long> itemIds = items.map(Item::getId).getContent();
        Map<Long, User> creatorMap = getItemCreatorMap(items.getContent());

        List<ItemResponse> itemResponses = items.map(item -> {
            return ModelMapper.mapItemToItemResponse(item,
                    creatorMap.get(item.getCreatedBy()));
        }).getContent();

        return new PagedResponse<ItemResponse>(
                itemResponses,
                items.getNumber(),
                items.getSize(),
                items.getTotalElements(),
                items.getTotalPages(),
                items.isLast()
        );
    }

    public PagedResponse<ItemResponse> getItemsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all items posted by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Item> items = itemRepository.findByCreatedBy(user.getId(), pageable);

        if (items.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), items.getNumber(),
                    items.getSize(), items.getTotalElements(), items.getTotalPages(), items.isLast());
        }

        // Map Items to ItemResponses
        List<Long> itemIds = items.map(Item::getId).getContent();
        Map<Long, User> creatorMap = getItemCreatorMap(items.getContent());

        List<ItemResponse> itemResponses = items.map(item -> {
            return ModelMapper.mapItemToItemResponse(item,
                    creatorMap.get(item.getCreatedBy()));
        }).getContent();

        return new PagedResponse<ItemResponse>(
                itemResponses,
                items.getNumber(),
                items.getSize(),
                items.getTotalElements(),
                items.getTotalPages(),
                items.isLast()
        );
    }

    public PagedResponse<ItemResponse> getItemsBiddedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all itemIds in which the given username has made a bid
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Long> userBiddedItemIds = bidRepository.findBiddedItemIdsByUserId(user.getId(), pageable);

        if (userBiddedItemIds.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), userBiddedItemIds.getNumber(),
                    userBiddedItemIds.getSize(), userBiddedItemIds.getTotalElements(),
                    userBiddedItemIds.getTotalPages(), userBiddedItemIds.isLast());
        }

        // Retrieve all poll details from the voted pollIds.
        List<Long> pollIds = userBiddedItemIds.getContent();

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Item> items = itemRepository.findByIdIn(pollIds, sort);

        //update later
        // Map Items to ItemResponses containing vote counts and poll creator details
        Map<Long, User> creatorMap = getItemCreatorMap(items);

        List<ItemResponse> itemResponses = items.stream().map(item -> {
            return ModelMapper.mapItemToItemResponse(item,
                    creatorMap.get(item.getCreatedBy()));
        }).collect(Collectors.toList());

        return new PagedResponse<ItemResponse>(
                itemResponses,
                userBiddedItemIds.getNumber(),
                userBiddedItemIds.getSize(),
                userBiddedItemIds.getTotalElements(),
                userBiddedItemIds.getTotalPages(),
                userBiddedItemIds.isLast()
        );
    }

    public Item postItem(ItemRequest itemRequest) {
        Item item = new Item();
        item.setItemName(itemRequest.getItemName());
        item.setDescription(itemRequest.getDescription());
        item.setUser(userRepository.getOne(itemRequest.getUserId()));

        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(itemRequest.getItemLength().getDays()))
                .plus(Duration.ofHours(itemRequest.getItemLength().getHours()));

        item.setExpirationDateTime(expirationDateTime);

        return itemRepository.save(item);
    }

    public ItemResponse getItemById(Long itemId, UserPrincipal currentUser) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("Item", "id", itemId));

        // Retrieve item creator details
        User creator = userRepository.findById(item.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", item.getCreatedBy()));

        return ModelMapper.mapItemToItemResponse(
                item,
                creator
        );
    }

    public ItemResponse makeBidAndGetUpdatedItem(Long itemId, BidRequest bidRequest, UserPrincipal currentUser) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        if(item.getExpirationDateTime().isBefore(Instant.now())) {
            throw new BadRequestException("Sorry! This Item listing has expired");
        }

        User user = userRepository.getOne(currentUser.getId());

        Bid bid = new Bid();
        bid.setBidVal(bidRequest.getBidVal());
        bid.setUser(user);
        bid.setItem(item);

        bid = bidRepository.save(bid);

        item.addBid(bid);
        itemRepository.save(item);

        // Bid posted, Return the updated Item Response
        // Retrieving item creator details
        User creator = userRepository.findById(item.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", item.getCreatedBy()));

        return ModelMapper.mapItemToItemResponse(item, creator);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    Map<Long, User> getItemCreatorMap(List<Item> items) {
        // Get Item Creator details of the given list of polls
        List<Long> creatorIds = items.stream()
                .map(Item::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}
