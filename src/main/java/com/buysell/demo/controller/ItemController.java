package com.buysell.demo.controller;

import com.buysell.demo.entity.Bid;
import com.buysell.demo.entity.Item;
import com.buysell.demo.model.BidDAO;
import com.buysell.demo.model.ItemDAO;
import com.buysell.demo.service.ItemService;
import com.buysell.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping(value = "/items")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

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
