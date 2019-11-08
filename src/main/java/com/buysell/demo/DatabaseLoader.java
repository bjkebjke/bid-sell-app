package com.buysell.demo;

import com.buysell.demo.entity.Item;
import com.buysell.demo.entity.User;
import com.buysell.demo.repository.BidRepository;
import com.buysell.demo.repository.ItemRepository;
import com.buysell.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository users;
    private final ItemRepository items;
    private final BidRepository bids;

    @Autowired
    public DatabaseLoader(UserRepository userRepository, ItemRepository itemRepository, BidRepository bidRepository) {
        this.users = userRepository;
        this.items = itemRepository;
        this.bids = bidRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        User bob = new User("bob", null, null, "bobpw", null);
        User jason = new User("jason", null, null, "jasonpw", null);

        Item jasonItem1 = new Item("item j1", null, jason.getId(), "jason's first item");
        Item jasonItem2 = new Item("item j2", null, jason.getId(), "jason's second item");

        Item bobItem1 = new Item("item b1", null, bob.getId(), "a fine first item");
        Item bobItem2 = new Item("item b2", null, bob.getId(), "bob's second item");

        List<Item> bobsitems = new ArrayList<>();
        List<Item> jasonsitems = new ArrayList<>();

        jasonsitems.add(jasonItem1);
        jasonsitems.add(jasonItem2);

        jason.setItems(jasonsitems);

        bobsitems.add(bobItem1);
        bobsitems.add(bobItem2);

        bob.setItems(bobsitems);


        User bobclone = this.users.save(bob);

        User jasonclone = this.users.save(jason);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("bob", "doesn't matter",
                        AuthorityUtils.createAuthorityList("SELLER")));


        Item item1clone = this.items.save(bobItem1);
        Item item2clone = this.items.save(bobItem2);
        Item item3 = this.items.save(jasonItem1);
        Item item4 = this.items.save(jasonItem2);

        SecurityContextHolder.clearContext();
    }
}
