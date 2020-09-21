package br.com.edsoft.methodtutorial;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.matcher.RestAssuredMatchers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
public class JsonSchemaTest {

	@Test
	public void deveValidaSchemaXML() {
		
		given()
		.log().all()
		.when()
		.get("https://restapi.wcaquino.me/usersXML")
		.then()
		.log().all()
		.statusCode(200)
		.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		
		;
	}

	
	@Test(expected = SAXParseException.class)
	public void naoDeveValidaSchemaXMLInvalido() {
		
		given()
		.log().all()
		.when()
		.get("https://restapi.wcaquino.me/invalidUsersXML")
		.then()
		.log().all()
		.statusCode(200)
		.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
		
		;
	}

	@Test(expected = SAXParseException.class)
	public void deveValidaSchemaJson() {
		
		given()
		.log().all()
		.when()
		.get("https://restapi.wcaquino.me/users")
		.then()
		.log().all()
		.statusCode(200)
		.body(RestAssuredMatchers.matchesXsdInClasspath("users.json"))
		
		;
	}

	
	
}
