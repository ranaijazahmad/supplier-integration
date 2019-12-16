package com.supplier.gateway.services;

import java.util.List;

import com.supplier.gateway.inventory.models.ProductModel;

public interface ProductService {

	public List<ProductModel> getProductInfo(List<String> partNumbers);
	public boolean updateItemsStock(List<ProductModel> productModels) throws Exception;
	

}
