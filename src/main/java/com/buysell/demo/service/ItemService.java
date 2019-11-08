package com.buysell.demo.service;

import com.buysell.demo.entity.Item;
import com.buysell.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    public void save(Item item) {
        itemRepository.save(item);
    }

    public List<Item> listAll() {
        return (List<Item>) itemRepository.findAll();
    }

    public Item get(Long id) {
        return itemRepository.findById(id).get();
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> getByUser_id(Long userid) {
        return itemRepository.findByUserid(userid);
    }
}
