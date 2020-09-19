package br.com.edsoft;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void olaMundo() {
		Response response = request(Method.GET, "https://restapi.wcaquino.me/ola?key=377643762");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue("A mensagem não é a mesma", response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertEquals(response.statusCode(), 200);
		try {
			ValidatableResponse validador = response.then();
			validador.statusCode(200);
		} catch (RuntimeException e) {
			System.out.println();
		}
	}

	@Test
	public void devoConhecerOutrasFormaRestAssured() {
		Response response = request(Method.GET, "https://restapi.wcaquino.me/ola?key=377643762");
		ValidatableResponse validador = response.then();
		validador.statusCode(200);

		get("https://restapi.wcaquino.me/ola").then().statusCode(200);

		given().when().get("https://restapi.wcaquino.me/ola").then().statusCode(200);
	}

	@Test
	public void devoConhecerMatcherHamcrest() {
		assertThat("Maria", Matchers.is("Maria"));
		assertThat(128, Matchers.is(128));
		assertThat(128, Matchers.isA(Integer.class));
		assertThat(128d, Matchers.isA(Double.class));
		assertThat(128d, Matchers.greaterThan(127d));
		assertThat(128d, Matchers.lessThan(130d));

		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, hasSize(5));
		assertThat(impares, containsInAnyOrder(1, 3, 5, 7, 9));
		assertThat(impares, hasItems(1, 5));
		assertThat("Maria", not("joao"));
		assertThat("maria", anyOf(is("maria"), is("joaqui")));
		assertThat("joaquina", allOf(startsWith("joa"), endsWith("ina"), containsString("qui")));
	}

	@Test
	public void devoValidarBody() {
		given().when().get("https://restapi.wcaquino.me/ola").then().statusCode(200).body(is("Ola Mundo!"))
				.body(containsString("Mundo")).body(is(not(nullValue())));
	}

	@Test
	public void deveVerificarPrimeiroNivel() {
		given().when().get("https://restapi.wcaquino.me/users/1").then().statusCode(200).body("id", is(1))
				.body("name", containsString("Silva")).body("age", greaterThan(18));
	}

	@Test
	public void deveVerificarPrimeiroNivelOutrasForma() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");
		Assert.assertEquals(new Integer(1), response.path("%s", "id"));
//		jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
//		from
		int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
	}

    @Test	
 	public void deveVerificarSegundoNivel() {
		given().
		when().
		get("https://restapi.wcaquino.me/users/2")
		.then()
		.statusCode(200)
		.body("id", is(2))
		.body("name", containsString("Joaquina"))
		.body("endereco.rua", is("Rua dos bobos"));

		
	}
	
	
	
	
	
}

