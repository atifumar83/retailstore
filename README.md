# retailstore


Pre-requisite

1. Java 8
2. Maven
3. MongoDb (retailstore dump added in parent forlder repo)

Assumption

1. Customer and product is already exist in mongodb, you can restore the retailstore dump from repository main folder
2. restore dump folder command,

     **mongorestore --host 10.201.201.104:27017  -u vidaptor -p Cs456321 --db retailstore  .\retailstore\**
     
     
Compile and Build:

1. open main retailstore directory
2. use the mention below command for compiling and for artifacts.

      mvn clean install.
      
      ![image](https://user-images.githubusercontent.com/38679516/177043464-7c5b6b45-667a-4275-92de-17fb327cd6c9.png)

      
3. Open target directory
4. Run the jar file with mention below command

      java -jar retailstore-0.0.1-SNAPSHOT.jar
      
      ![image](https://user-images.githubusercontent.com/38679516/177043483-97239ba3-4fb6-4f4d-8f12-2a02908eb682.png)
      

API for Testing.

1. API for creating invoice http://localhost:8080/retail/invoice/?customerPhone=0512141372
2. You can send body the mention below 

              [
              {
                  "itemName" : "dovesoap",
                  "productCode" : "DOV789",
                  "itemCategory" : "GROCERY",
                  "quantity" : 1,
                  "itemPrice" : "10"
              },
              {
                  "itemName" : "laptop",
                  "productCode" : "SAMSUNG789",
                  "itemCategory" : "OTHERS",
                  "quantity" : 1,
                  "itemPrice" : "1000"
              }
              ]

3. it will return the totalBill and discount applied on each item.


Unit Test Cases.

1. Added unit test case base on mockito, where we can test the discount applied and invoice creation.

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









