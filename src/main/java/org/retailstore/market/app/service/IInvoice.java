package org.retailstore.market.app.service;

import java.util.List;

import org.retailstore.market.app.model.Customer;
import org.retailstore.market.app.model.Invoice;
import org.retailstore.market.app.model.Product;
import org.springframework.data.domain.Page;

public interface IInvoice {

	public Invoice createInvoice(List<Product> products,String customerPhone);
	
	public Page<Invoice> getInvoiceByCustomer(Customer customer);
	
	public List<Product> calculateDiscountBaseOnCustomerType(List<Product> products,Customer customer);
}
