
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReUsableMethods;
import files.payload;
public class basics_2 {

	public static void main(String[] args)
	{
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response = given().queryParam("key", "qaclick123").header("Content-Type","application/json")
				.body(payload.AddPlace()).when().post("maps/api/place/add/json")
				.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
				.header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();

		System.out.println(response);
		JsonPath js = new JsonPath(response);	
		String placeId= js.getString("place_id");

		//update

		String newAddress = "Summer Walk, Africa";

		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "").when().post("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

		//getplace

		String getPlaceResponse= given().log().all()
				.queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json")
				.then().assertThat().log().all().statusCode(200).extract().response().asString();

		//JsonPath js1 = new JsonPath(response);

		JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);

		String actualAddress= js1.getString("address");

		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress,newAddress );



	}

}
