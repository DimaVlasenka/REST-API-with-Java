import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

	public static void main(String[] args) {
		
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setName("Dimona Practice");
		ap.setPhone_number("(+91) 555 983 893 3937");
		ap.setAddress("299, side layout, cohen 09");
		ap.setWebsite("http://google.com");
		ap.setLanguage("Polish-IN");
		
		List<String> myList = new ArrayList<String>();
		myList.add("teaching park");
		myList.add("shopping");
		
		ap.setTypes(myList);
		
		Location loc = new Location();
		loc.setLat(-39.383494);
		loc.setLng(34.427362);
		
		ap.setLocation(loc);
		
		RequestSpecification requestObject = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").
		setContentType(ContentType.JSON).build();
		
		ResponseSpecification responseObject = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		//RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//We can store request part in separate request object of RequestSpecification class
		
		RequestSpecification addPlaceRequest = given().log().all().spec(requestObject).body(ap);
		
		String addPlaceResponse = addPlaceRequest.
		when().post("maps/api/place/add/json").
		then().spec(responseObject).extract().response().asString();
		
		System.out.println("-------");
		System.out.println(addPlaceResponse);

	}

}
