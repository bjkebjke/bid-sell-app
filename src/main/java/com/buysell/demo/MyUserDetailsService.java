package com.buysell.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private final SellerRepository sellers;
    private final BuyerRepository buyers;

    @Autowired
    public MyUserDetailsService(SellerRepository sellers, BuyerRepository buyers) {
        this.sellers = sellers;
        this.buyers = buyers;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        if(this.sellers.findByName(name)!=null) {
            Seller seller = this.sellers.findByName(name);
            return new User(seller.getName(), seller.getPassword(),
                    AuthorityUtils.createAuthorityList(seller.getRoles()));
        } else {
            Buyer buyer = this.buyers.findByName(name);
            return new User(buyer.getName(), buyer.getPassword(),
                    AuthorityUtils.createAuthorityList(buyer.getRoles()));
        }
    }
}
