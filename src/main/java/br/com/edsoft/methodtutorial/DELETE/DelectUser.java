package br.com.edsoft.methodtutorial.DELETE;

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

public class DelectUser {

	@Test
	public void deveRemoverUsuario() {
		given().
		log().all()
		.when()
		.delete("https://restapi.wcaquino.me/users/1")
		.then()
		.log().all()
		.statusCode(204)
		
		
		;
		
		
	}

	@Test
	public void NaoDeveRemoverUsuarioInexistente() {
		given().
		log().all()
		.when()
		.delete("https://restapi.wcaquino.me/users/100")
		.then()
		.log().all()
		.statusCode(400)
		.body("error", is("Registro inexistente"))
		
		
		;
		
		
	}
	
	
	
	
	
}
