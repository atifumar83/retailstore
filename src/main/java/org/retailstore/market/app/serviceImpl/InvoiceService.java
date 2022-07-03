package org.retailstore.market.app.serviceImpl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.retailstore.market.app.model.Customer;
import org.retailstore.market.app.model.Invoice;
import org.retailstore.market.app.model.Product;
import org.retailstore.market.app.repo.InvoiceRepository;
import org.retailstore.market.app.service.ICustomerService;
import org.retailstore.market.app.service.IInvoice;
import org.retailstore.market.app.settings.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService implements IInvoice{

	@Autowired
	private ICustomerService customerService;
	
	@Autowired
	private InvoiceRepository invoiceRepo;
	
	/**
	 * Passing products to create invoice against customer phone and applying discount base on type customer,employee and affiliate
	 */
	@Override
	public Invoice createInvoice(List<Product> products,String customerPhone) {
		if(products.size()==0)
			return null;
		Customer customer = customerService.getCustomerInformation(customerPhone);//Find Customer information base on phone
		Invoice invoice = new Invoice();
		this.calculateDiscountBaseOnCustomerType(products, customer);
		this.calcualteTotalBill(products, invoice, customer);
		this.discountOnTotalBill(invoice);
		return invoiceRepo.save(invoice);
	}
	
	public List<Product> calculateDiscountBaseOnCustomerType(List<Product> products,Customer customer) {
		double discount;
		if(this.checkCustomerHistory(customer)) {//if user has been customer for 2 years
			discount=5;//5% discount
		}else {
			discount = Double.parseDouble(customer.getDiscount().replaceAll("[^a-zA-Z0-9]",""));//Check customer discount
		}
		for(Product product:products) {
			switch(product.getItemCategory()) {
			case GROCERY://discount is not applying
				product.setDiscountApplied(0);
			break;
			case OTHERS:
			  double discountApplied = Utils.calculateDiscount(product.getItemPrice(), discount);
			  product.setDiscountApplied(discountApplied);
			break;
			}
		}
		return products;
	}
	
	
	/**
	 * If user has been customer for 2 years 
	 */
	private boolean checkCustomerHistory(Customer customer) {
		Page<Invoice> latestInvoice = this.getInvoiceByCustomer(customer);
		long currentTime = System.currentTimeMillis();
		if(latestInvoice.getContent().size()>0) {
			long timeDiff = Math.abs(currentTime - latestInvoice.getContent().get(0).getDateTime());

			long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
			
			if(daysDiff==730) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @param products selected
	 * @param invoice  selected Invoice
	 * @param customer
	 * 
	 * @return It will caculdate the total bill amount base on discount
	 */
	public Invoice calcualteTotalBill(List<Product> products,Invoice invoice,Customer customer) {
		Double totalAmount = products.stream()
				  .map(x -> x.getItemPrice())
				  .reduce(0.0, Double::sum);
		Double totalDiscountApplied = products.stream()
				  .map(x -> x.getDiscountApplied())
				  .reduce(0.0, Double::sum);
		Double totalBillAmount = totalAmount-totalDiscountApplied;
		invoice.setCustomer(customer);
		invoice.setProducts(products);
		invoice.setTotalBill(totalBillAmount);
		
		return invoice;
	}
	/**
	 * For every 100$ bill, he will get 5$ dicount
	 * @param invoice
	 * @return
	 */
	private Invoice discountOnTotalBill(Invoice invoice) {
		if(invoice.getTotalBill()>100) {
			 double discountApplied = Utils.calculateDiscount(invoice.getTotalBill(), 5);
			 invoice.setTotalBill(invoice.getTotalBill()-discountApplied);
		}
		return invoice;
	}

	/**
	 * Get latest invoice history by customer
	 */
	@Override
	public Page<Invoice> getInvoiceByCustomer(Customer customer) {
		org.springframework.data.domain.Pageable paging = PageRequest.of(0, 1, Sort.by(Direction.ASC, "dateTime"));
		return this.invoiceRepo.findByCustomer(customer.getCustomerMobile(), paging);
	}

}
