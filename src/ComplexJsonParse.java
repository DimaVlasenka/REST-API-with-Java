import org.testng.Assert;

import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		
		JsonPath js = new JsonPath(payload.CourcePrice());
		
		//Print Number of courses
		System.out.println("-------");
		int countCourses = js.getInt("courses.size()");
		System.out.println(countCourses);
		
		//Print Purchase amount
		System.out.println("-------");
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		
		//Print Title of the first course
		String title = js.getString("courses[0].title");
		System.out.println(title);

		//4. Print All course titles and their respective Prices
		System.out.println("-------");
		for (int i = 0; i<countCourses; i++) {
			String courseTitle = js.getString("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			System.out.println(courseTitle + " : " + coursePrice);
		}

		//5. Print no of copies sold by RPA Course
		System.out.println("-------");
		for (int i = 0; i<countCourses; i++) {
			String courseTitle = js.getString("courses["+i+"].title");
			if (courseTitle.equals("RPA")) {
				int courseCopies = js.getInt("courses["+i+"].copies");
				System.out.println(courseTitle + " : " + courseCopies);
				break;
			}			
		}

		//6. Verify if Sum of all Course prices matches with Purchase Amount
		System.out.println("-------");
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
