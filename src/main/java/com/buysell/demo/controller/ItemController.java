package com.buysell.demo.controller;

import com.buysell.demo.entity.Bid;
import com.buysell.demo.entity.Item;
import com.buysell.demo.model.BidDAO;
import com.buysell.demo.model.ItemDAO;
import com.buysell.demo.payload.*;
import com.buysell.demo.repository.BidRepository;
import com.buysell.demo.repository.ItemRepository;
import com.buysell.demo.repository.UserRepository;
import com.buysell.demo.security.CurrentUser;
import com.buysell.demo.security.UserPrincipal;
import com.buysell.demo.service.ItemService;
import com.buysell.demo.service.UserService;
import com.buysell.demo.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@Controller
@RequestMapping(value = "/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    UserService userService;

    @GetMapping
    public PagedResponse<ItemResponse> getItems(@CurrentUser UserPrincipal currentUser,
                                                @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return itemService.getAllItems(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> postItem(@Valid @RequestBody ItemRequest itemRequest) {
        Item item = itemService.postItem(itemRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{pollId}")
                .buildAndExpand(item.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Item Uploaded Successfully"));
    }

    @GetMapping("/{itemId}")
    public ItemResponse getItemById(@CurrentUser UserPrincipal currentUser,
                                    @PathVariable Long itemId) {
        return itemService.getItemById(itemId, currentUser);
    }

    @PostMapping("/{itemId}/bids")
    @PreAuthorize("hasRole('USER')")
    public ItemResponse makeBid(@CurrentUser UserPrincipal currentUser,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody BidRequest bidRequest) {
        return itemService.makeBidAndGetUpdatedItem(itemId, bidRequest, currentUser);
    }



    //old




    /*
    @RequestMapping(value = "/{id}")
    public String itemPage(Model model, @PathVariable("id") Long itemId) {
        Item i = itemService.get(itemId);

        if(i.getBids().size()>0){
            Bid maxBid = Collections.max(i.getBids());
            model.addAttribute("maxBid", maxBid);
            model.addAttribute("existsMax", true);
        } else {
            model.addAttribute("existsMax", false);
        }

        ItemDAO itemDAO = new ItemDAO(i.getItemName(), i.getDescription());
        BidDAO bidDAO = new BidDAO(itemId);

        model.addAttribute("item", itemDAO);
        model.addAttribute("bid", bidDAO);


        return "basicitem";
    }
    */

    @RequestMapping(value = "/new")
    public String newItem(Model model, Authentication auth) {
        ItemDAO item = new ItemDAO();

        model.addAttribute("item", item);

        return "newitem";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String saveItem(@ModelAttribute ItemDAO dao, Authentication auth) {
        Item item = new Item(dao.getName(), null, userService.get(auth.getName()).getId(), dao.getDescription());

        itemService.save(item);

        return "success";
    }
}
