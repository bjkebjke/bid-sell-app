package com.buysell.demo;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface BuyerRepository extends Repository<Buyer, Long> {

    Buyer save(Buyer buyer);

    Buyer findByName(String name);
}
