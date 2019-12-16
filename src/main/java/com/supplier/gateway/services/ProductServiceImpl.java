package com.supplier.gateway.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supplier.gateway.inventory.entities.Product;
import com.supplier.gateway.inventory.models.ProductModel;
import com.supplier.gateway.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public List<ProductModel> getProductInfo(List<String> partNumbers) {
		List<ProductModel> productModels = new ArrayList<>();
		List<Product> prodcuts = productRepository.findProductByPartNumberIn(partNumbers);
		if (prodcuts != null && !prodcuts.isEmpty()) {
			for (Product product : prodcuts) {
				if (product != null) {
					ProductModel productModel = new ProductModel();
					productModel.setAvailableQuantity(product.getAvailableQty());
					productModel.setPartNumber(product.getPartNumber());
					productModel.setDescription(product.getDescription());

					productModels.add(productModel);
				}
			}
		}
		return productModels;
	}

	@Override
	public boolean updateItemsStock(List<ProductModel> productModels) throws Exception{
		List<Product> prodcuts = new ArrayList<>();
		boolean productUpdated = false;
		try {
			if(productModels != null && !productModels.isEmpty()) {
				List<String> partNumbers = productModels.stream().map(ProductModel::getPartNumber).collect(Collectors.toList());		
				prodcuts = productRepository.findProductByPartNumberIn(partNumbers);
				if(prodcuts != null && !prodcuts.isEmpty()) {
					for(Product product: prodcuts) {
						for(ProductModel productModel: productModels) {
							Integer availableQty = product.getAvailableQty();
							Integer requiredQty = productModel.getAvailableQuantity();
							if(product.getPartNumber().equals(productModel.getPartNumber())) {
								if(isEnoughQtyAvailable(availableQty, requiredQty)) {
									product.setAvailableQty((availableQty.intValue() - requiredQty.intValue()));
									productUpdated = true;
									break;
								}else {
									throw new IllegalStateException("Required Quanitity is not available for Item " + product.getPartNumber());
								}								
							}
						}
					}
				}
			}		
			
			if(productUpdated) {
				return !(productRepository.saveAll(prodcuts)).isEmpty();
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong while updating the items, please contact your system administrator", e);
		}
		
		return false;
	}

	private boolean isEnoughQtyAvailable(Integer availableQty, Integer requiredQty) {
		return availableQty.compareTo(requiredQty) >= 0;
	}

}
