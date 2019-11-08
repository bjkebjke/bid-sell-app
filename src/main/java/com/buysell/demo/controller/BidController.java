package com.buysell.demo.controller;

import com.buysell.demo.entity.Bid;
import com.buysell.demo.model.BidDAO;
import com.buysell.demo.service.BidService;
import com.buysell.demo.service.ItemService;
import com.buysell.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/bids")
public class BidController {
    @Autowired
    BidService bidService;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveBid(@ModelAttribute BidDAO bDAO, Authentication auth) {
        Bid bid = new Bid(bDAO.getBidVal(), bDAO.getItemId(), userService.get(auth.getName()).getId());

        bidService.save(bid);

        return "success";
    }

}
