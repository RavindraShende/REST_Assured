package Rest_Assured.REST_API;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static org.assertj.core.api.Assertions.*;

public class BookerApi {
	
	RequestSpecification requestSpecification;
	Response response;
	String tokenId;
	Integer bookingId;
	  
@Test(priority=0)
public void HealthCheack() {
requestSpecification=RestAssured.given();
requestSpecification.baseUri("https://restful-booker.herokuapp.com");
requestSpecification.basePath("/ping");
response=requestSpecification.when().get();
response.then().log().all();
int a=response.statusCode();
assertThat(a).isNotNull().isNotNegative().isNotZero();

}
@Test(priority=1)
public void Token() {
	String tokenPayload="{\r\n"
			+ "    \"username\" : \"admin\",\r\n"
			+ "    \"password\" : \"password123\"\r\n"
			+ "}";
	requestSpecification=RestAssured.given();
	requestSpecification.baseUri("https://restful-booker.herokuapp.com");
	requestSpecification.basePath("/auth");
	requestSpecification.body(tokenPayload);
	requestSpecification.contentType("application/json");
	response=requestSpecification.when().post();
	response.then().log().all();
	 tokenId=response.then().extract().path("token");
	System.out.println("Created Token ID-----"+    tokenId);
	
	
	
}
@Test(priority=2)
public void createBooking() {
	String CreatePayload="{\r\n"
			+ "    \"firstname\" : \"Jim\",\r\n"
			+ "    \"lastname\" : \"Brown\",\r\n"
			+ "    \"totalprice\" : 111,\r\n"
			+ "    \"depositpaid\" : true,\r\n"
			+ "    \"bookingdates\" : {\r\n"
			+ "        \"checkin\" : \"2018-01-01\",\r\n"
			+ "        \"checkout\" : \"2019-01-01\"\r\n"
			+ "    },\r\n"
			+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
			+ "}";
	requestSpecification=RestAssured.given();
	requestSpecification.baseUri("https://restful-booker.herokuapp.com");
	requestSpecification.basePath("/booking");
	requestSpecification.body(CreatePayload);
	requestSpecification.contentType("application/json");
	requestSpecification.accept("application/json");
	response=requestSpecification.when().post();
	response.then().log().all();
	bookingId=response.then().extract().path("bookingid");
	System.out.print("Extracted BookingID====="   +bookingId);
	
}



	@Test(priority=3)
	public void updateBooking(){
		String updatePayload="{\r\n"
				+ "    \"firstname\" : \"James\",\r\n"
				+ "    \"lastname\" : \"Brown\",\r\n"
				+ "    \"totalprice\" : 111,\r\n"
				+ "    \"depositpaid\" : true,\r\n"
				+ "    \"bookingdates\" : {\r\n"
				+ "        \"checkin\" : \"2018-01-01\",\r\n"
				+ "        \"checkout\" : \"2019-01-01\"\r\n"
				+ "    },\r\n"
				+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
				+ "}";
		requestSpecification=RestAssured.given();
		requestSpecification.contentType(ContentType.JSON);
		requestSpecification.accept("application/json");
		requestSpecification.cookie("token",tokenId);
		requestSpecification.baseUri("https://restful-booker.herokuapp.com");
		requestSpecification.basePath("/booking/1");
		requestSpecification.body(updatePayload);
		response=requestSpecification.when().put();
		response.then().log().all();
		String firstname=response.then().extract().path("firstname");
		System.out.println("Updated Firstname"+firstname);
		
		
	}
	
	@Test(priority=4 ,dependsOnMethods= {"createBooking"})
	public void GetBooking() {
		requestSpecification=RestAssured.given();
		requestSpecification.accept("application/json");
		requestSpecification.baseUri("https://restful-booker.herokuapp.com");
		requestSpecification.basePath("/booking/"+ bookingId);
		response=requestSpecification.when().get();
		response.then().log().all();
	}

}
