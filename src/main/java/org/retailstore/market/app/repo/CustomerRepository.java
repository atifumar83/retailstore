package org.retailstore.market.app.repo;

import org.retailstore.market.app.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> ,QuerydslPredicateExecutor<Customer>{

	public Customer findByCustomerMobile(String customerMobile);
	
}
