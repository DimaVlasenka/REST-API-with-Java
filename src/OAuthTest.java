import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import static io.restassured.RestAssured.*;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourses;
import pojo.WebAutomation;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		
		//to get access code
		System.setProperty("webdriver.chrome.driver", "src/files/chromedriver.exe");
				
		WebDriver driver = new ChromeDriver();
		String authorizationURL = "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=dimavtest";
		driver.get(authorizationURL);
		String myEmail = "dimonavalenska@gmail.com";
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(myEmail);
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		
		Thread.sleep(4000);
		
		String myPassword = "blabla123456";
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(myPassword);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
		
		Thread.sleep(2000);
		
		String currentURL = driver.getCurrentUrl();
		
		String partialCode = currentURL.split("code=")[1];
		String accessCode = partialCode.split("&scope")[0];
		
		//to get access token
		//urlEncodingEnabled(false) method prevents special character like % to be converted into numeric value
		String accessTokenResponse = given().urlEncodingEnabled(false).
		queryParam("code", accessCode).
		queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
		queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W").
		queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php").
		queryParam("grant_type", "authorization_code").
		when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath jsAccessToken = new JsonPath(accessTokenResponse);
		String accessToken = jsAccessToken.getString("access_token");
		
		//to get list of courses 
		
		/*String listCoursesResponse = given().queryParam("access_token", accessToken).
		when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();
		
		System.out.println(listCoursesResponse);*/
		
		GetCourses mainObject = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).
				when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourses.class);
		
		System.out.println(mainObject.getInstructor());
		System.out.println(mainObject.getLinkedIn());
		
		//Print title and price of "SoapUI Webservices testing"
		String courseToFind = "SoapUI Webservices testing";
		
		List<Api> api = mainObject.getCourses().getApi();
		
		for(int i=0;i<api.size();i++) {
			
			if(api.get(i).getCourseTitle().equalsIgnoreCase(courseToFind)) {
				System.out.println(api.get(i).getCourseTitle());
				System.out.println(api.get(i).getPrice());
				break;
			}
		}
		
		//Print all courses titles and prices from webAutomation
		
		List<WebAutomation> webAutomation = mainObject.getCourses().getWebAutomation();
		for(int h = 0; h < webAutomation.size(); h++) {
			System.out.println(webAutomation.get(h).getCourseTitle());
			System.out.println(webAutomation.get(h).getPrice());
			
		//Compare WebAutomation courses with expected predefined list
			String[] expectedCourses = {"Selenium Webdriver Java","Cypress", "Protractor"};
			ArrayList<String> actualCourses = new ArrayList<String>();
			
			for (int j = 0; j < webAutomation.size(); j++) {
				actualCourses.add(j, webAutomation.get(j).getCourseTitle());
			}
			
			List<String> expectedList = Arrays.asList(expectedCourses);
			
			Assert.assertTrue(actualCourses.equals(expectedList));
			
		}
		
		driver.quit();
		
	}

}
