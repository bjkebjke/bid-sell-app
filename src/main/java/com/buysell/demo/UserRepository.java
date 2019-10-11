package com.buysell.demo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserRepository {

    User save(User seller);

    User findByName(String name);
}
