package com.buysell.demo;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(exported = false)
public interface UserRepository extends Repository<User, Long>{

    User save(User seller);

    User findByName(String name);
}
