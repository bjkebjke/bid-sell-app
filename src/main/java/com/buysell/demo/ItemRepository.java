package com.buysell.demo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {
    @Override
    @PreAuthorize("#item?.seller == null or #item?.seller?.name == authentication?.name")
    Item save(@Param("item") Item item);

    @Override
    @PreAuthorize("@itemRepository.findById(#id)?.seller?.name == authentication?.name")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#item?.seller?.name == authentication?.name")
    void delete(@Param("item") Item item);
}
