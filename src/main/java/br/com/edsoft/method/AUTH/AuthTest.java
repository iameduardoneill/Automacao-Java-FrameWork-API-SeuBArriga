package br.com.edsoft.method.AUTH;

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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matcher.*;


public class AuthTest {

	@Test
	public void deveAcessarSWAPI() {
		given()
		.when()
		.get("https://swapi.dev")
		.then()
		.log().all()
		.statusCode(200)
		.body("name", is("Luke Skywalker"))
		
		;
	}

	@Test
	public void deveObterClima() {
		given()
		.log().all()
		.queryParam("q", "Fortaleza,BR")
		.queryParam("appid","f65fcc4996d690a0c27386be715de723")
		.queryParam("units","metrics")
		.when()
		.get("http://api.openweathermap.org/data/2.5/weather")
		.then()
		.log().all()
		.statusCode(200)
        .body("name", is("Fortaleza"))
        .body("coord.lon", is(-38.52f))
        .body("main.temp", greaterThan(25f))
		
		;
		//http://api.openweathermap.org/data/2.5/weather?q=Fortaleza,BR&appid=f65fcc4996d690a0c27386be715de723&&units=metrics
	}

	@Test
	public void naoDeveAcessaSemSenha() {
		given()
		.log().all()
		.when()
		.get("http://restapi.wcaquino.me/basicauth")
		.then()
		.log().all()
		.statusCode(401)
		
		;
	}

	@Test
	public void deveFazerAutenticacaoBasica() {
		given()
		.log().all()
		.when()
		.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
		.log().all()
		.statusCode(200)
		.body("status", is("logado"))
		
		;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() {
		given()
		.log().all()
		.auth().basic("admin", "senha")
		.when()
		.get("http://@restapi.wcaquino.me/basicauth")
		.then()
		.log().all()
		.statusCode(200)
		.body("status", is("logado"))
		
		;
	}

	@Test
	public void deveFazerAutenticacaoBasica3Challenge() {
		given()
		.log().all()
		.auth().preemptive().basic("admin", "senha")
		.when()
		.get("http://restapi.wcaquino.me/basicauth2")
		.then()
		.log().all()
		.statusCode(200)
		.body("status", is("logado"))
		
		;
	}

	@Test
	public void deveFazerAutenticacaoComTokenJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "ed@ed.com");
	    login.put("senha", "eduhit00");
	    
	    //login na api
	    //receber o token
		String token = given()
		.log().all()
		.body(login)
		.contentType(ContentType.JSON)
		.when()
		.post("http://barrigarest.wcaquino.me/signin")
		.then()
		.log().all()
		.statusCode(200)
		.extract().path("token")
		;
		
		given()
		.log().all()
		.header("Authorization","JWT "+token)
		.when()
		.get("http://barrigarest.wcaquino.me/contas")
		.then()
		.log().all()
		.statusCode(200)
        .body("nome", hasItem("Conta de teste"))
		;
		
	}
	
}
