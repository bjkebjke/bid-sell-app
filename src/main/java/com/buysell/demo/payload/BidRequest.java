package com.buysell.demo.payload;

import javax.validation.constraints.NotNull;

public class BidRequest {
    @NotNull
    private int bidVal;

    @NotNull
    private Long itemId;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getBidVal() {
        return bidVal;
    }

    public void setBidVal(int bidVal) {
        this.bidVal = bidVal;
    }
}
