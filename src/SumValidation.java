import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public void sumOfCourses() {
		
		JsonPath js = new JsonPath(payload.CourcePrice());
		
		int countCourses = js.getInt("courses.size()");
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		
		int sum = 0;
		for (int i = 0; i<countCourses; i++) {
			int coursePrice = js.getInt("courses["+i+"].price");
			int courseCopies = js.getInt("courses["+i+"].copies");
			int total = coursePrice*courseCopies;
			sum = sum + total;						
		}
		
		System.out.println("Sum is: " + sum);
		System.out.println("Purchase amount is: " + purchaseAmount);
		Assert.assertEquals(sum, purchaseAmount);
	}

}
