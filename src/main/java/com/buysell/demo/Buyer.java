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
@Table(name = "buyers")
public class Buyer {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private @Id @GeneratedValue Long id;
    private String name;

    @OneToMany(
            mappedBy = "buyers",
            orphanRemoval = true
    )
    private List<Bid> bids = new ArrayList<>();

    private @JsonIgnore String password;

    //private @Version @JsonIgnore Long version;

    private String[] roles;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    protected Buyer() {}

    public Buyer(String name, List<Bid> bids, String password, String[] roles) {
        this.name = name;
        this.bids = bids;
        this.setPassword(password);
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buyer)) return false;
        Buyer buyer = (Buyer) o;
        return Objects.equals(getId(), buyer.getId()) &&
                Objects.equals(getName(), buyer.getName()) &&
                Objects.equals(getBids(), buyer.getBids()) &&
                Objects.equals(getPassword(), buyer.getPassword()) &&
                Arrays.equals(getRoles(), buyer.getRoles());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getName(), getBids(), getPassword());
        result = 31 * result + Arrays.hashCode(getRoles());
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

    public String getPassword() {
        return password;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Buyer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bids=" + bids +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
