package com.buysell.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository users;

    @Autowired
    public MyUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        String n = this.users.findByName(name).getName();
        String pw = this.users.findByName(name).getPassword();
        String[] roles = this.users.findByName(name).getRoles();

        return new User(n, pw, AuthorityUtils.createAuthorityList(roles));
    }
}
