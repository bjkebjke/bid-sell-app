package com.buysell.demo;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SellerRepository {

    Seller save(Seller seller);

    Seller findByName(String name);
}
