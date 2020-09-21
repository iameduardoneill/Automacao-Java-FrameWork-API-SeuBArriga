package br.com.edsoft.methodtutorial.GET;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class EnviaDadosViaQueryTest {

	
	@Test
	public void deveEnviarValorQuery() {
		given().
		log().all().
		when().
//		get("https://restapi.wcaquino.me/v2/users?format=xml").
		get("https://restapi.wcaquino.me/v2/users?format=json").
		then()
		.log().all()
		.statusCode(200)
//		.contentType(ContentType.XML)
		.contentType(ContentType.JSON)
		;
	}

	@Test
	public void deveEnviarValorQueryViaParam() {
		given().
		log().all().
		queryParam("format", "xml").
		when().
		get("https://restapi.wcaquino.me/v2/users").
		then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.XML)
		.contentType(containsString("utf-8"))
		
		;
	}
	
	@Test
	public void deveEnviarValorViaHeader() {
		given().
		log().all().
		accept(ContentType.XML).
		when().
		get("https://restapi.wcaquino.me/v2/users").
		then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.XML)
		;
	}
	
	
	
}
