import io.restassured.RestAssured;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SerializeTest {

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
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String addPlaceResponse = given().log().all().queryParam("key", "qaclick123").body(ap).
		when().post("maps/api/place/add/json").
		then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println("-------");
		System.out.println(addPlaceResponse);

	}

}
