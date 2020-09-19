package br.com.edsoft;

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
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class JsonPathTest {

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
    
    
    @Test
    public void deveVerificarTerceiroNivel() {
    	given().
		when().
		get("https://restapi.wcaquino.me/users/3")
		.then()
		.statusCode(200)
		.body("id", is(3))
		.body("name", containsString("Ana"))
		.body("filhos", hasSize(2))
		.body("filhos[0].name", is("Zezinho"))
		.body("filhos[1].name", is("Luizinho"))
		.body("filhos.name", hasItem("Zezinho"))
		.body("filhos.name", hasItems("Zezinho", "Luizinho"))
		;
    }

    @Test
    public void deveRetornaErroUsuarioInesxistente() {
    	given().
    	when().
    	get("https://restapi.wcaquino.me/users/4")
    	.then()
    	.statusCode(404)
        .body("error", is("Usuário inexistente"))
    ;
    }

    @Test
    public void deveVerificarListaRaiz() {
    	given().
    	when().
    	get("https://restapi.wcaquino.me/users")
    	.then()
    	.statusCode(200)
    	.body("$", hasSize(3))
    	.body("name", hasItems("João da Silva","Maria Joaquina","Ana Júlia"))
    	.body("filhos.name", hasItem(Arrays.asList("Zezinho","Luzinho")))
    	.body("salary", contains(1234.5678f, 2500, null));
    }

    @Test
    public void deveFazerVerificoesAvancadas() {
    	given().
    	when().
    	get("https://restapi.wcaquino.me/users")
    	.then()
    	.statusCode(200)
    	.body("$", hasSize(3))
        .body("age.findAll{it <= 25}.size()", is(2))    	
        .body("age.findAll{it <= 25 && it > 20}.size()", is(1))    	
        .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))    	
        .body("findAll{it.age <= 25}[-1].name", is("Ana Júlia"))    	
        .body("find{it.age <= 25}.name", is("Maria Joaquina"))
        .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina","Ana Júlia"))
        .body("findAll{it.name.length()>10}.name", hasItems("João da Silva","Maria Joaquina"))
        .body("name.collect{it.toUpperCase()}",hasItem("MARIA JOAQUINA"))
        .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",hasItem("MARIA JOAQUINA"))
        .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()",allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
        .body("age.collect{it * 2}", hasItems(60,50,40))    	
        .body("id.max()", is(3))
        .body("salary.min()", is(1234.5678f))
        .body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f,0.001)))
        .body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
        
        
    	;
    }

    @Test
    public void devoUnirJsonPathComJava() {
    	ArrayList<String> names = 
    	
    	given().
    	when().
    	get("https://restapi.wcaquino.me/users")
    	.then()
    	.statusCode(200)
    	.body("$", hasSize(3))
        .extract().path("name.findAll{it.startsWith('Maria')}")    	
    	
    	
    	;
    	Assert.assertEquals(1, names.size());
    	Assert.assertTrue(names.get(0).equalsIgnoreCase("Maria Joaquina"));
    	Assert.assertEquals(names.get(0).toUpperCase(),"maria joaquina".toUpperCase());
    	
    }
	

    
    
	
	
	
}

