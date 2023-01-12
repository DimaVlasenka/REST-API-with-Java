import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*; //package needed for equalTo() method

import org.testng.Assert;

import files.payload;
import static io.restassured.RestAssured.*; //package needed for given() method

public class Basics {

	public static void main(String[] args) {
		
		//to validate if Add place API works as expected
		
		// Add place -> Update place with new address -> Get place to validate if new address is presented in response
		
		//Given - all input details query/path parameters, header, body
		//When - submit the API - resource and http method are specified here 
		//Then - validate the response
		
		//Setup Base URL
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//given, when, then implementation
		//Add place
		String addPlaseResponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body(payload.AddPlace())
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
				
		System.out.println(addPlaseResponse);
		
		JsonPath jsAdd = new JsonPath(addPlaseResponse); //for parsing Json
		
		String placeId = jsAdd.getString("place_id");
		
		System.out.println(placeId);
		
		//Update place
		String newAddress = "10943 Tailwater, Belgium";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json").body(payload.UpdatePlace(placeId,newAddress))
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get place
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).header("Server", "Apache/2.4.41 (Ubuntu)").body("address", equalTo(newAddress));
		
		//Get place - Alternative assertion
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath jsGet = new JsonPath(getPlaceResponse);
		String actualAddress = jsGet.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress, newAddress);
		
	}

}
