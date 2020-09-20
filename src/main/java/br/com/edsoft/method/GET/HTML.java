package br.com.edsoft.method.GET;

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

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


public class HTML {

	@Test
	public void deveFazerBuscasComHTML() {
		given().
		log().all().
		when().
		get("https://restapi.wcaquino.me/v2/users").
		then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.HTML)
		.body("html.body.div.table.tbody.tr.size()", is(3))
		.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
		.appendRootPath("hmtl.body.div.table.tbody")
		.body("tr.find{it.toString().startsWith('2')}.td[1]",is("Maria Joaquina"))
		
		;
	}

	@Test
	public void deveFazerBuscasComXpathEmHTML() {
		given().
		log().all().
		when().
		get("https://restapi.wcaquino.me/v2/users?format=clean").
		then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.HTML)
        .body(hasXPath("count(//table/tr)",is("4")))
        .body(hasXPath("//td[text()='2']/../td[2]",is("Maria Joaquina")))
		
		
		
		;
	}
	
	
	
	
}
