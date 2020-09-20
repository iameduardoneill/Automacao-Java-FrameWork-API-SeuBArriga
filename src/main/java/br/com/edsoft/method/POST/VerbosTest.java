package br.com.edsoft.method.POST;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.edsoft.method.User;
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

public class VerbosTest {

	@Test
	public void deveSalvaUsuario() {
		given().
		log().all()
		.contentType("application/json")
		.body("{\"name\": \"Jose\", \"age\":50}").
		when().
		post("https://restapi.wcaquino.me/users").
		then()
		.log().all()
		.statusCode(201)
		.body("id",is(notNullValue()))
		.body("name",is(50))
		
		
		;
	}

	@Test
	public void deveSalvaUsuarioUsandoMAP() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 25);
		
		given().
		log().all()
		.contentType("application/json")
		.body(params).
		when().
		post("https://restapi.wcaquino.me/users").
		then()
		.log().all()
		.statusCode(201)
		.body("id",is(notNullValue()))
		.body("name",is("Usuario via map"))
		.body("age", is(25))
		
		;
	}

	@Test
	public void deveSalvaUsuarioUsandoObject() {
	User  user = new User("Usuario via Objecto",25);
		
		given().
		log().all()
		.contentType("application/json")
		.body(user).
		when().
		post("https://restapi.wcaquino.me/users").
		then()
		.log().all()
		.statusCode(201)
		.body("id",is(notNullValue()))
		.body("name",is("Usuario via Objecto"))
		.body("age", is(25))
		
		;
	}

	@Test
	public void deveDeserializarObjectoAoSalvaSalvaUsuario() {
		User  user = new User("Usuario deserializado",25);
		
		User usuarioInserido = given().
		log().all()
		.contentType("application/json")
		.body(user).
		when().
		post("https://restapi.wcaquino.me/users").
		then()
		.log().all()
		.statusCode(201)
        .extract().body().as(User.class)		
		
        
        ;
        Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", user.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(25));
	}

	@Test
	public void naoDeveSalvaUsuarioSemName() {
		given().
		log().all()
		.contentType("application/json")
		.body("{\"age\":50}").
		when().
		post("https://restapi.wcaquino.me/users").
		then()
		.log().all()
		.statusCode(400)
		.body("id",is(nullValue()))
		.body("error",is("Name é um atributo obrigatório"))
		
		
		;
	}
	
	
	@Test
	public void deveSalvaViaXMLUsuario() {
		given().
		log().all()
		.contentType(ContentType.XML)
		.body("<user><name>Jose</name><age>50</age></user>").
		when().
		post("https://restapi.wcaquino.me/usersXML").
		then()
		.log().all()
		.statusCode(201)
		.body("user.@id",is(notNullValue()))
		.body("user.name",is("Jose"))
		.body("user.age",is("50"))
		
		
		;
	}

	@Test
	public void deveSalvaViaXMLUsuarioUsandoObjeto() {
	User user = new User("Usuario XML", 40);
		given().
		log().all()
		.contentType(ContentType.XML)
		.body(user).
		when().
		post("https://restapi.wcaquino.me/usersXML").
		then()
		.log().all()
		.statusCode(201)
		.body("user.@id",is(notNullValue()))
		.body("user.name",is("Usuario XML"))
		.body("user.age",is("40"))
		
		
		;
	}

	@Test
	public void deveDeserializarXMLAoSalvaViaXMLUsuario() {
		User user = new User("Usuario XML", 40);
		User usuarioInserido = given().
		
				log().all()
		.contentType(ContentType.XML)
		.body(user).
		when().
		post("https://restapi.wcaquino.me/usersXML").
		then()
		.log().all()
		.statusCode(201)
        .extract().body().as(User.class)
		
		;
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("Usuario XML"));
		Assert.assertThat(usuarioInserido.getAge(), is(40));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue());
	}
	
}






