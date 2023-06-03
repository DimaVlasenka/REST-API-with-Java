import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

public class ECommerceAPITest {

	public static void main(String[] args) {

		// Login
		RequestSpecification reqBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUserEmail("dima@gmail.com");
		loginRequest.setUserPassword("Dm_123456");

		// NOTE relaxedHTTPSValidation() is used to bypass SSL certification
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(reqBasic).body(loginRequest);

		LoginResponse loginResponse = reqLogin.when().log().all().post("/api/ecom/auth/login").then().log().all()
				.extract().response().as(LoginResponse.class);
		System.out.println(loginResponse.getToken());
		System.out.println(loginResponse.getUserId());

		String userID = loginResponse.getUserId();
		String userToken = loginResponse.getToken();

		// Add product //multiPart() is used to add attachment

		RequestSpecification reqAddPoductBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", userToken).build();

		RequestSpecification reqAddProduct = given().log().all().spec(reqAddPoductBasic)
				.param("productName", "LaptopDimona").param("productAddedBy", userID)
				.param("productCategory", "electronic").param("productSubCategory", "Laptops")
				.param("productPrice", "1795").param("productDescription", "DimonaNewBrand")
				.param("productFor", "Everybody").multiPart("productImage",
						new File("C://Users//DV//eclipse-workspace//DemoProject//src//files//laptop.jpg"));

		String addPRoductResponse = reqAddProduct.when().log().all().post("/api/ecom/product/add-product").then().log()
				.all().extract().response().asString();

		JsonPath js = new JsonPath(addPRoductResponse);

		String productID = js.get("productId");

		System.out.println(productID);

		// URL to access UI part https://rahulshettyacademy.com/client "userEmail":
		// "dima@gmail.com", "userPassword": "Dm_123456"

		// Create Order

		RequestSpecification reqCreateOrderBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", userToken).setContentType(ContentType.JSON).build();

		OrderDetails orderDetail = new OrderDetails();
		orderDetail.setCountry("Germany");
		orderDetail.setProductOrderedId(productID);

		List<OrderDetails> listOrderDetails = new ArrayList<OrderDetails>();
		listOrderDetails.add(orderDetail);

		Orders orders = new Orders();
		orders.setOrders(listOrderDetails);

		RequestSpecification reqCreateOrder = given().log().all().spec(reqCreateOrderBasic).body(orders);

		String createOrderResponse = reqCreateOrder.when().post("/api/ecom/order/create-order").then().log().all()
				.extract().response().asString();
		
		JsonPath js1 = new JsonPath(createOrderResponse);
		List <String> ListOrderID = js1.getList("orders");
		
		String orderID = ListOrderID.get(0);
		
		System.out.println(orderID);
		
		//Delete Product
		
		RequestSpecification deleteProdBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
				addHeader("Authorization", userToken).setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteProdReq = given().log().all().spec(deleteProdBasic).pathParam("productID", productID);
		
		String deleteProdResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productID}").
				then().log().all().extract().response().asString();
		
		JsonPath js2 = new JsonPath(deleteProdResponse);
		String deleteMessage = js2.getString("message");
		
		Assert.assertEquals(deleteMessage, "Product Deleted Successfully");
		
		//Delete Order
		
		RequestSpecification deleteOrderBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
				addHeader("Authorization", userToken).setContentType(ContentType.JSON).build();
		
		RequestSpecification deleteOrderReq = given().spec(deleteOrderBasic).pathParam("OrderID", orderID);
		
		String deleteOrderResponse = deleteOrderReq.when().delete("https://rahulshettyacademy.com/api/ecom/order/delete-order/{OrderID}").then().log().all().extract().response().asString();
		
		JsonPath js3 = new JsonPath(deleteOrderResponse);
		
		String deleteOrderMessage = js3.getString("message");
		
		Assert.assertEquals(deleteOrderMessage, "Orders Deleted Successfully");
		
	}

}
