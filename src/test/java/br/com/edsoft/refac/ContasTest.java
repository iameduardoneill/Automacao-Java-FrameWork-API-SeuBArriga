package br.com.edsoft.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.edsoft.rest.core.BaseTest;
import br.com.edsoft.rest.utils.BarrigaUtils;
import io.restassured.RestAssured;

public class ContasTest extends BaseTest{

	
	
	
	
	@Test
	public void deveIncluirContaComSucesso() {
		
     given()
		.body("{\"nome\": \"Conta inserida\"}")
		.when()
		.post("/contas")
		.then()
        .statusCode(201)
		;
	}
	
	@Test
	public void deveAlterarContaComSucesso() {
	Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para alterar");
		
		given()
		.body("{\"nome\": \"Conta alterada\"}")
		.pathParam("id", CONTA_ID)
		.when()
		.put("/contas/{id}")
		.then()
		.log().all()
		.statusCode(200)
	    .body("nome", is("Conta alterada"))

	    ;
	}
	
	@Test
	public void naoDeveInserirContaComMesmoNome() {
		given()
		.body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
		.post("/contas")
		.then()
		.statusCode(400)
		.body("error",is("J� existe uma conta com esse nome!"))
		;
	}
	
	
}
