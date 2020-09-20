package br.com.edsoft.method.PUT;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.RestAssured.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matcher.*;

public class UpdateUser {

	@Test
	public void deveAlteraUsuario() {
		given().
		log().all()
		.contentType("application/json")
		.body("{\"name\": \"Usuario alterado\", \"age\":80}").
		when().
		put("https://restapi.wcaquino.me/users/1").
		then()
		.log().all()
		.statusCode(200)
		.body("id",is(1))
		.body("name",is("Usuario alterado"))
		.body("age",is(80))
		.body("salary",is(1234.5678f))
		
		
		;
	}

	@Test
	public void deveCustamizarURL() {
		given().
		log().all()
		.contentType("application/json")
		.body("{\"name\": \"Usuario alterado\", \"age\":80}").
		when().
		put("https://restapi.wcaquino.me/{entidade}/{userId}", "users","1").
		then()
		.log().all()
		.statusCode(200)
		.body("id",is(1))
		.body("name",is("Usuario alterado"))
		.body("age",is(80))
		.body("salary",is(1234.5678f))
		
		
		;
	}

	@Test
	public void deveCustamizarURLPart2() {
		given().
		log().all()
		.contentType("application/json")
		.body("{\"name\": \"Usuario alterado\", \"age\":80}").
		pathParam("entidade", "users").
		pathParam("userId", 1).
		when().
		put("https://restapi.wcaquino.me/{entidade}/{userId}").
		then()
		.log().all()
		.statusCode(200)
		.body("id",is(1))
		.body("name",is("Usuario alterado"))
		.body("age",is(80))
		.body("salary",is(1234.5678f))
		
		
		;
	}
}
