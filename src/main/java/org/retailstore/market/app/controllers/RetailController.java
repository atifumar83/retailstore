package org.retailstore.market.app.controllers;

import java.util.List;

import org.retailstore.market.app.model.Invoice;
import org.retailstore.market.app.model.Product;
import org.retailstore.market.app.service.IInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/retail")
public class RetailController {

	@Autowired
	private IInvoice invoiceService;
	/**
	 * 
	 * @param products
	 * @param customerPhone
	 * @return will return invoice base on customer type and product category GROCERY, OTHERS
	 */
	@RequestMapping(value = "/invoice/", method = RequestMethod.POST)
	public ResponseEntity<Invoice> addOrder(@RequestBody List<Product> products,@RequestParam(required = true) String customerPhone) {
		Invoice invoice = invoiceService.createInvoice(products, customerPhone);
		return new ResponseEntity<>(invoice, HttpStatus.OK);
	}
	
	
}
