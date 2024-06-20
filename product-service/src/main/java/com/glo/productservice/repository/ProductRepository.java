package com.glo.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.glo.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
