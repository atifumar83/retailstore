package org.retailstore.market.app.serviceImpl;

import java.util.List;

import org.retailstore.market.app.model.Product;
import org.retailstore.market.app.repo.ProductRepository;
import org.retailstore.market.app.service.IProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProduct{

	@Autowired
	private ProductRepository productRepo;
	
	@Override
	public Product findByProductCode(String productCode) {
		return productRepo.findByProductCode(productCode);
	}

	@Override
	public List<Product> findAll() {
		return productRepo.findAll();
	}

}
