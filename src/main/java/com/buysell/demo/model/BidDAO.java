package com.buysell.demo.model;

public class BidDAO {
    private int bidVal;

    private Long userId;

    private Long itemId;

    public BidDAO() {

    }

    public BidDAO(int bidVal, Long userId, Long itemId) {
        this.bidVal = bidVal;
        this.userId = userId;
        this.itemId = itemId;
    }

    public BidDAO(Long itemId) {
        this.itemId = itemId;
    }

    public int getBidVal() {
        return bidVal;
    }

    public void setBidVal(int bidVal) {
        this.bidVal = bidVal;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}
