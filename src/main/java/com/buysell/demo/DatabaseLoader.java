package com.buysell.demo;

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
        User bob = new User("bob", null, null, "bobpw", "SELLER");

        Item bobItem1 = new Item("item b1", null, bob);
        Item bobItem2 = new Item("item b2", null, bob);

        List<Item> bobsitems = new ArrayList<>();

        bobsitems.add(bobItem1);
        bobsitems.add(bobItem2);

        bob.setItems(bobsitems);


        User bobclone = this.users.save(bob);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("bob", "doesn't matter",
                        AuthorityUtils.createAuthorityList("SELLER")));


        this.items.save(bobItem1);
        this.items.save(bobItem2);

        SecurityContextHolder.clearContext();
    }
}
