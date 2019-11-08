package com.buysell.demo.repository;

import com.buysell.demo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Override
    Item save(@Param("item") Item item);

    @Override
    void deleteById(@Param("id") Long id);

    @Override
    void delete(@Param("item") Item item);

    List<Item> findByUserid(Long userid);

    // new

    // find top bid for itemid?

    Optional<Item> findById(Long pollId);

    Page<Item> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Item> findByIdIn(List<Long> pollIds);

    List<Item> findByIdIn(List<Long> pollIds, Sort sort);
}
