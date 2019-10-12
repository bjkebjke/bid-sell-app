package com.buysell.demo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String itemName;

    private String description;

    @OneToMany(
            mappedBy = "item",
            orphanRemoval = true
    )
    private List<Bid> bids = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    private Item() {}

    public Item(String itemName, List<Bid> bids, User seller, String description) {
        this.itemName = itemName;
        this.bids = bids;
        this.seller = seller;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId()) &&
                Objects.equals(getItemName(), item.getItemName()) &&
                Objects.equals(description, item.description) &&
                Objects.equals(getBids(), item.getBids()) &&
                Objects.equals(getSeller(), item.getSeller());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getItemName(), description, getBids(), getSeller());
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

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", bids=" + bids +
                ", seller=" + seller +
                '}';
    }
}
