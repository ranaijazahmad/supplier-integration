package com.supplier.gateway.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supplier.gateway.inventory.entities.Product;

@Repository
public interface ProductRepository extends GenericRepository<Product, Integer> {
	public List<Product> findProductByPartNumberIn(List<String> partNumber);
}
