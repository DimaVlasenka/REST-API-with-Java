import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

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
		
		Thread.sleep(4000);
		
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
		
		String listCoursesResponse = given().queryParam("access_token", accessToken).
		when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();
		
		System.out.println(listCoursesResponse);
		
		driver.quit();
		
	}

}
