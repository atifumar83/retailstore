package org.retailstore.market.mocktest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.retailstore.market.app.RetailStore;
import org.retailstore.market.app.model.Customer;
import org.retailstore.market.app.model.Invoice;
import org.retailstore.market.app.model.Product;
import org.retailstore.market.app.service.ICustomerService;
import org.retailstore.market.app.service.IInvoice;
import org.retailstore.market.app.service.IProduct;
import org.retailstore.market.app.serviceImpl.CustomerService;
import org.retailstore.market.app.serviceImpl.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@SpringBootTest(classes = RetailStore.class)
public class RetailStoreMockTest {

	@Autowired
	ICustomerService customerService;

	@Autowired
	IProduct productService;

	@Autowired
	IInvoice invoice;

	@Mock
	IInvoice invoiceService;

	/**
	 * Assumtion: Customer and Product already exist in DB Load customer with
	 * phoneNumber
	 * 
	 * @param customerMobile
	 */
	@ParameterizedTest
	@ValueSource(strings = { "0582141372", "0512141372" })
	public void loadCustomers(String customerMobile) {
		Customer customer = customerService.getCustomerInformation(customerMobile);
		CustomerService mock = org.mockito.Mockito.mock(CustomerService.class);
		when(mock.getCustomerInformation(customerMobile)).thenReturn(customer);

		Assert.assertEquals(customerMobile, customer.getCustomerMobile());
	}
	
	/**
	 * Applying customer discount base on product type
	 * @param customerMobile
	 */

	@ParameterizedTest
	@ValueSource(strings = { "0582141372", "0512141372" })
	public void applyDiscountBaseOnCustomerCategory(String customerMobile) {

		Product product = productService.findByProductCode("SAMSUNG789");
		product.setDiscountApplied(50.0);
		Customer customer = customerService.getCustomerInformation(customerMobile);

		InvoiceService mock = org.mockito.Mockito.mock(InvoiceService.class);
		List<Product> products = new ArrayList<Product>();

		products.add(product);
		when(mock.calculateDiscountBaseOnCustomerType(products, customer)).thenReturn(products);
		assertTrue(invoice.calculateDiscountBaseOnCustomerType(products, customer).get(0)
				.getDiscountApplied() == product.getDiscountApplied());
	}

	/**
	 * creating invoice base on customer category
	 * @param customerMobile
	 */
	@ParameterizedTest
	@ValueSource(strings = { "0582141372", "0512141372" })
	public void createInvoice(String customerMobile) {

		ObjectMapper jsonConvertor = new ObjectMapper();

		Product product = productService.findByProductCode("SAMSUNG789");

		Product product1 = productService.findByProductCode("DOV789");
		List<Product> products = new ArrayList<Product>();
		products.add(product);

		Customer customer = customerService.getCustomerInformation(customerMobile);
		Invoice invoice = this.invoice.createInvoice(products, customer.getCustomerMobile());

		try {
			String result = jsonConvertor.writeValueAsString(invoice);
			System.out.println("******************************INVOICE**************************************");
			System.out.println(result);
			System.out.println("******************************INVOICE END**************************************");

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//        Assert.assertEquals(invoice.getCustomer().getCustomerMobile(), customer.getCustomerMobile());
	}
}
