package org.retailstore.market.app.service;

import java.util.List;

import org.retailstore.market.app.model.Product;

public interface IProduct {

	public Product findByProductCode(String productCode);
	
	public List<Product> findAll();
	
}
