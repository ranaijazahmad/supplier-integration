package com.supplier.gateway.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.supplier.gateway.inventory.models.ProductModel;
import com.supplier.gateway.services.ProductService;

@RestController
public class InventoryController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/stock/items", method = RequestMethod.POST)
	public List<ProductModel> checkStock(@RequestBody List<String> partNumbers) {
		List<ProductModel> productModels = productService.getProductInfo(partNumbers);
		return productModels;
	}
	
	@RequestMapping(value = "/stock/items", method = RequestMethod.PUT)
	public String updateStock(@RequestBody List<ProductModel> productModels) {
		
		boolean status = false;
		try {
			status = productService.updateItemsStock(productModels);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return status?"SUCCESS":"ERROR";
	}
}
