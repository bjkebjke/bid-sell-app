package com.buysell.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final SellerRepository sellers;
    private final BuyerRepository buyers;
    private final ItemRepository items;
    private final BidRepository bids;

    @Autowired
    public DatabaseLoader(SellerRepository sellerRepository, BuyerRepository buyerRepository,
                          ItemRepository itemRepository, BidRepository bidRepository) {
        this.sellers = sellerRepository;
        this.buyers = buyerRepository;
        this.items = itemRepository;
        this.bids = bidRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

    }
}
