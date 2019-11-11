package com.buysell.demo.model;

import com.buysell.demo.model.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bids")
public class Bid extends DateAudit implements Comparable<Bid> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int bidVal;

    private Long itemid;

    private Long userid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonBackReference
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public Bid() {}

    public Bid(int bidVal, Long itemid, Long userid) {
        this.bidVal = bidVal;
        this.itemid = itemid;
        this.userid = userid;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Long getItemid() {
        return itemid;
    }

    public void setItemid(Long itemid) {
        this.itemid = itemid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return bidVal == bid.bidVal &&
                Objects.equals(id, bid.id) &&
                Objects.equals(itemid, bid.itemid) &&
                Objects.equals(userid, bid.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bidVal, itemid, userid);
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidVal=" + bidVal +
                ", itemid=" + itemid +
                ", userid=" + userid +
                '}';
    }

    @Override
    public int compareTo(Bid o) {
        return Integer.compare(this.bidVal, o.bidVal);
    }
}
