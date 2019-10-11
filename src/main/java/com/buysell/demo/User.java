package com.buysell.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private @Id
    @GeneratedValue
    Long id;
    private String name;

    @OneToMany(
            mappedBy = "users",
            orphanRemoval = true
    )
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(
            mappedBy = "users",
            orphanRemoval = true
    )
    private List<Item> items = new ArrayList<>();

    private @JsonIgnore
    String password;

    //private @Version @JsonIgnore Long version;

    private String[] roles;

    protected User() {}

    public User(String name, List<Bid> bids, List<Item> items, String password, String[] roles) {
        this.name = name;
        this.bids = bids;
        this.items = items;
        this.password = password;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(bids, user.bids) &&
                Objects.equals(items, user.items) &&
                Objects.equals(password, user.password) &&
                Arrays.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, bids, items, password);
        result = 31 * result + Arrays.hashCode(roles);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bids=" + bids +
                ", items=" + items +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
