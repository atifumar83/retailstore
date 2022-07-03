package org.retailstore.market.app.serviceImpl;

import org.retailstore.market.app.model.Customer;
import org.retailstore.market.app.repo.CustomerRepository;
import org.retailstore.market.app.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private CustomerRepository customerRepo;
	
	@Override
	public Customer getCustomerInformation(String mobileNumber) {
		if(mobileNumber.equalsIgnoreCase("")) {
			return new Customer();
		}
		return customerRepo.findByCustomerMobile(mobileNumber);
	}

}
