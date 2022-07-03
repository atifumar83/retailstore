package org.retailstore.market.app.repo;

import org.retailstore.market.app.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> ,QuerydslPredicateExecutor<Invoice>{

	@Query(value="{'customer.customerMobile': ?0}")
	public Page<Invoice> findByCustomer(String phoneNumber,Pageable page);
	
}
