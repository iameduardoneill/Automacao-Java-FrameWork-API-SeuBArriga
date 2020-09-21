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

public class SaldoTest extends BaseTest{

	
	
	@Test
	public void deveCalcularSaldoContas() {
	Integer CONTA_ID = BarrigaUtils.getIdContaPeloNome("Conta para saldo");
		
		
		given()
		.when()
		.get("/saldo")
		.then()
		.statusCode(200)
		.body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))
		;
	}
	
	
}
