package com.buysell.demo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue
    private Long id;

    private int bidVal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Bid() {}

    public Bid(Long id, int bidVal, User buyer, Item item) {
        this.id = id;
        this.bidVal = bidVal;
        this.buyer = buyer;
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bid)) return false;
        Bid bid = (Bid) o;
        return bidVal == bid.bidVal &&
                Objects.equals(id, bid.id) &&
                Objects.equals(buyer, bid.buyer) &&
                Objects.equals(item, bid.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bidVal, buyer, item);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBidVal() {
        return bidVal;
    }

    public void setBidVal(int bidVal) {
        this.bidVal = bidVal;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidVal=" + bidVal +
                ", buyer=" + buyer +
                ", item=" + item +
                '}';
    }
}
