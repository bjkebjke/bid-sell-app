package com.buysell.demo.payload;

import java.time.Instant;

public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Long itemCount;
    private Long bidCount;

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long itemCount, Long bidCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.itemCount = itemCount;
        this.bidCount = bidCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getitemCount() {
        return itemCount;
    }

    public void setitemCount(Long itemCount) {
        this.itemCount = itemCount;
    }

    public Long getbidCount() {
        return bidCount;
    }

    public void setbidCount(Long bidCount) {
        this.bidCount = bidCount;
    }
}
