package br.com.edsoft.refac;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.edsoft.rest.core.BaseTest;
import io.restassured.RestAssured;

@RunWith(Suite.class)
@SuiteClasses({ ContasTest.class, MovimentacaoTest.class, SaldoTest.class ,AuthTest.class})
public class AllTests extends BaseTest {
	
	@BeforeClass
	public static void login() {
		System.out.println("Carrega method login");
		Map<String, String> login = new HashMap<>();
		login.put("email", "ed@ed.com");
		login.put("senha", "eduhit00");
		
	String	TOKEN = given()
				.body(login)
				.when()
				.post("/signin")
				.then()
				.statusCode(200)
				.extract().path("token");
		
		
		
		RestAssured.requestSpecification.header("Authorization", "JWT "+TOKEN);
	RestAssured.get("/reset").then().statusCode(200);
	}

}
