package com.buysell.demo.repository;

import com.buysell.demo.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    // find top bid for itemid?

    Optional<Item> findById(Long itemId);

    Page<Item> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Item> findByIdIn(List<Long> itemIds);

    List<Item> findByIdIn(List<Long> itemIds, Sort sort);
}
