package org.retailstore.market.app.service;

import org.retailstore.market.app.model.Customer;

public interface ICustomerService {

	public Customer getCustomerInformation(String mobileNumber);
	
}
