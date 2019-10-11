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
@Table(name="sellers")
public class Seller {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private @Id
    @GeneratedValue
    Long id;

    private String name;

    @OneToMany(
            mappedBy = "sellers",
            orphanRemoval = true
    )
    private List<Item> items = new ArrayList<>();

    private @JsonIgnore String password;

    private String[] roles;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public Seller(String name, List<Item> items, String password, String[] roles) {
        this.name = name;
        this.items = items;
        this.setPassword(password);
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seller)) return false;
        Seller seller = (Seller) o;
        return Objects.equals(id, seller.id) &&
                Objects.equals(name, seller.name) &&
                Objects.equals(items, seller.items) &&
                Objects.equals(password, seller.password) &&
                Arrays.equals(roles, seller.roles);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, items, password);
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
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
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", items=" + items +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
