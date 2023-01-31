import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import files.payload;

public class JiraTest {
	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:9080";
		
		//Login
		
		SessionFilter session = new SessionFilter();
		
		String loginResponse =  given().log().all().relaxedHTTPSValidation().header("Content-Type", "application/json").body(payload.JiraLogin()).
		filter(session).		
		when().post("/rest/auth/1/session").
		then().log().all().extract().response().asString();
		
		// Add Comment
		String expectedComment = "This is comment added through the automation script";
		String addCommentResponse = given().log().all().pathParam("key", "DIM-1").header("Content-Type", "application/json").body(payload.AddJiraComment(expectedComment)).
		filter(session).
		when().post("rest/api/2/issue/{key}/comment").
		then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentID = js.getString("id");
		
		//Add Attachment
		given().log().all().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key", "DIM-1").header("Content-Type", "multipart/form-data").
		multiPart("file", new File("C:\\Users\\DV\\eclipse-workspace\\DemoProject\\src\\files\\Info.txt")).
		when().post("/rest/api/2/issue/{key}/attachments").
		then().log().all().assertThat().statusCode(200);
		
		//get issue details
		String getIssueResponse = given().log().all().filter(session).pathParam("key", "DIM-1").queryParam("fields", "comment").
		when().get("/rest/agile/1.0/issue/{key}").
		then().log().all().extract().response().asString();
		
		JsonPath jsGetIssue = new JsonPath(getIssueResponse);
		int commentsCount = jsGetIssue.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++) 
		{
			String commentIDActual = jsGetIssue.getString("fields.comment.comments["+i+"].id");
			if(commentIDActual.equals(commentID)) 
			{
				String messageActual = jsGetIssue.getString("fields.comment.comments["+i+"].body");
				Assert.assertEquals(messageActual, expectedComment);
				System.out.println(messageActual);
				break;
			}
			
			
		}
		

	}

}
