package com.buysell.demo.payload;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class ItemRequest {
    @NotBlank
    @Size(max = 40)
    private String itemName;

    @NotBlank
    @Size(max = 100)
    private String description;

    @NotNull
    @Valid
    private ItemLength itemLength;

    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemLength getItemLength() {
        return itemLength;
    }

    public void setItemLength(ItemLength itemLength) {
        this.itemLength = itemLength;
    }
}
