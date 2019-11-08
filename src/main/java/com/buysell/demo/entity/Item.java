package com.buysell.demo.entity;

import com.buysell.demo.entity.audit.UserDateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item extends UserDateAudit {

    @Id
    @GeneratedValue
    @Column(name = "itemid")
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String itemName;

    @NotBlank
    @Size(max = 100)
    private String description;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "itemid", referencedColumnName = "itemid")
    private List<Bid> bids = new ArrayList<>();

    // edit later?
    private Bid topBid;

    private Long userid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    private Instant expirationDateTime;

    public Item() {}

    public Item(String itemName, List<Bid> bids, Long userid, String description) {
        this.itemName = itemName;
        this.bids = bids;
        this.userid = userid;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", bids=" + bids +
                ", userid=" + userid +
                '}';
    }

    public Bid getTopBid() {
        return topBid;
    }

    public void setTopBid(Bid topBid) {
        this.topBid = topBid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(Instant expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
