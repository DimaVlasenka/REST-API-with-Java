import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static org.hamcrest.Matchers.*;

import files.payload;

import static io.restassured.RestAssured.*;

public class Basics {

	public static void main(String[] args) {
		
		//to validate if Add place API works as expected
		
		//Given - all input details
		//When - submit the API - resource and http method are specified here 
		//Then - validate the response
		
		//Setup Base URL
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//given, when, then implementation
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(payload.AddPlace())
		.when().post("/maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = new JsonPath(response); //for parsing Json
		
		String placeId = js.getString("place_id");
		
		System.out.println(placeId);



	}

}