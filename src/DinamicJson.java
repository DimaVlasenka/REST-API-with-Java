import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DinamicJson 
{
	
	@Test (dataProvider = "BooksSet")
	public void addBook(String isbn, String aisle) 
	{
		RestAssured.baseURI = "http://216.10.245.166";
		
		String addBookResponse = given().log().all().header("Content-Type", "application/json").body(payload.AddBook(isbn,aisle))
		.when().post("Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200).body("Msg", equalTo("successfully added")).extract().response().asString();
		
		JsonPath js = new JsonPath(addBookResponse);
		String bookID = js.getString("ID");
		System.out.println("BookId is: " + bookID);
	}
	
	@DataProvider (name="BooksSet")
	public Object[][] getDataSet() 
	{
		//array - collection of elements
		//multidimensional array - collection of arrays 
		
		return new Object[][] {{"qwe","326"},{"asd","327"},{"zxc","328"}};//to create an object of multidimensional array
	}
	

}
