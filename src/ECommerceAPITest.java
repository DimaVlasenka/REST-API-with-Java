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

public class ECommerceAPITest {

	public static void main(String[] args) {
	
	//Login	
	RequestSpecification reqBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
	
	LoginRequest loginRequest = new LoginRequest();
	loginRequest.setUserEmail("dima@gmail.com");
	loginRequest.setUserPassword("Dm_123456");
	
	RequestSpecification reqLogin = given().log().all().spec(reqBasic).body(loginRequest);
	
	LoginResponse loginResponse = reqLogin.when().log().all().post("/api/ecom/auth/login").then().log().all().extract().response().as(LoginResponse.class);
	System.out.println(loginResponse.getToken());
	System.out.println(loginResponse.getUserId());
	
	String userID = loginResponse.getUserId();
	String userToken = loginResponse.getToken();
	
	//Add product //multiPart() is used to add attachment
	
	RequestSpecification reqAddPoductBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("Authorization", userToken).build();
			
	RequestSpecification reqAddProduct = given().log().all().spec(reqAddPoductBasic).
			param("productName", "LaptopDimona").
			param("productAddedBy", userID).
			param("productCategory", "electronic").
			param("productSubCategory", "Laptops").
			param("productPrice","1795").
			param("productDescription", "DimonaNewBrand").
			param("productFor","Everybody").
			multiPart("productImage", new File("C://Users//DV//eclipse-workspace//DemoProject//src//files//laptop.jpg"));
	
	
String addPRoductResponse =	reqAddProduct.when().log().all().post("/api/ecom/product/add-product").then().log().all().extract().response().asString();

JsonPath js = new JsonPath(addPRoductResponse);

String productID = js.get("productId");

System.out.println(productID);

	// URL to access UI part https://rahulshettyacademy.com/client "userEmail": "dima@gmail.com", "userPassword": "Dm_123456"
	
	//Create Order

RequestSpecification reqCreateOrderBasic = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
addHeader("Authorization", userToken).setContentType(ContentType.JSON).build();

OrderDetails orderDetail = new OrderDetails();
orderDetail.setCountry("Germany");
orderDetail.setProductOrderedId(productID);

List<OrderDetails> listOrderDetails = new ArrayList<OrderDetails>();
listOrderDetails.add(orderDetail);

Orders orders = new Orders();
orders.setOrders(listOrderDetails);

RequestSpecification reqCreateOrder = given().log().all().spec(reqCreateOrderBasic).body(orders);

String createOrderResponse = reqCreateOrder.when().post("/api/ecom/order/create-order").
then().log().all().extract().response().asString();




	}

}
