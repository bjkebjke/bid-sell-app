package com.buysell.demo.repository;

import com.buysell.demo.model.Bid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    /*
    @Query("SELECT b FROM Bid b where b.item.id = :itemId ORDER BY b.bidVal LIMIT 1")
    Bid findMaxBidByItemId(@Param("itemId") Long itemId);
    */

    Bid findTop1ByItemIdOrderByBidValDesc(Long itemId);

    @Query("SELECT b FROM Bid b where b.user.id = :userId and b.item.id in :itemIds")
    List<Bid> findByUserIdAndItemIdIn(@Param("userId") Long userId, @Param("itemIds") List<Long> itemIds);

    @Nullable
    @Query("SELECT b FROM Bid b where b.user.id = :userId and b.item.id = :itemId")
    Bid findByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    @Query("SELECT COUNT(b.id) from Bid b where b.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT b.item.id FROM Bid b WHERE b.user.id = :userId")
    Page<Long> findBiddedItemIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    Boolean existsBidByUseridAndItemid(Long userId, Long itemId);

}
