package com.buysell.demo.service;

import com.buysell.demo.entity.Bid;
import com.buysell.demo.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BidService {
    @Autowired
    BidRepository bidRepository;

    public void save(Bid bid) {
        bidRepository.save(bid);
    }

    public List<Bid> listAll() {
        return (List<Bid>) bidRepository.findAll();
    }

    public Bid get(Long id) {
        return bidRepository.findById(id).get();
    }

    public void delete(Long id) {
        bidRepository.deleteById(id);
    }
}
