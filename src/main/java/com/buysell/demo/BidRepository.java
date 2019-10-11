package com.buysell.demo;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BidRepository extends PagingAndSortingRepository<Bid, Long> {
}
