package com.buysell.demo.controller;

import com.buysell.demo.entity.Item;
import com.buysell.demo.model.ItemDAO;
import com.buysell.demo.service.ItemService;
import com.buysell.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/home")
    public String home(Model model) {
        List<Item> items = itemService.listAll();
        model.addAttribute("items", items);

        return "home";
    }

    @RequestMapping(value = "/myitems")
    public String myitems(Model model, Authentication auth) {
        //List<Item> myitems = userService.get(auth.getName()).getItems();
        List<Item> myitems = itemService.getByUser_id(userService.get(auth.getName()).getId());

        model.addAttribute("myitems", myitems);

        return "myitems";
    }
}
