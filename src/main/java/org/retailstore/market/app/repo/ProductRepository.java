package org.retailstore.market.app.repo;

import org.retailstore.market.app.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends MongoRepository<Product, String> ,QuerydslPredicateExecutor<Product>{

	public Product findByProductCode(String productCode);
	
}
