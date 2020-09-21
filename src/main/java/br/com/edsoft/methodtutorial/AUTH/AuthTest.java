package br.com.edsoft.methodtutorial.AUTH;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;


public class AuthTest {

	@Test
	public void deveAcessarSWAPI() {
		given()
		.when()
		.get("https://swapi.dev")
		.then()
		.log().all()
		.statusCode(200)
		
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

	@Test
	public void deveAcessarAplicacaoWeb() {
		
		//login na api
		//receber o token
		 String cookie = given()
				.log().all()
				.formParam("email", "ed@ed.com")
				.formParam("senha", "eduhit00")
				.contentType(ContentType.URLENC.withCharset("UTF-8"))
				.when()
				.post("http://seubarriga.wcaquino.me/logar")
				.then()
				.log().all()
				.statusCode(200)
				.extract().header("set-cookie")
				;
		
		 cookie = cookie.split("=")[1].split(";")[0];
		 System.out.println(cookie);
	
		 String body = given()
					.log().all()
					.cookie("connect.sid",cookie)
					.when()
					.get("http://seubarriga.wcaquino.me/contas")
					.then()
					.log().all()
					.statusCode(200)
					.body("html.body.table.tbody.tr[0].td[0]", is("Conta de teste"))
					.extract().body().asString();
					;
					
	 System.out.println("-------------------------------");				
     XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);			
	 System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));	 
		 
		
	}
	
	
	
	
}
