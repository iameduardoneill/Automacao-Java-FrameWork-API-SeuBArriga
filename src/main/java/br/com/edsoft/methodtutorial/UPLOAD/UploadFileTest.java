package br.com.edsoft.methodtutorial.UPLOAD;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.lowagie.text.pdf.codec.Base64.OutputStream;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


public class UploadFileTest {
    @Test 
	public void deveObrigarEnviaArquivo() {
		
		given()
		.log().all()
		.when()
		.post("http://restapi.wcaquino.me/upload")
		.then()
		.log().all()
		.statusCode(404)
		.body("error", is("Arquivo não enviado"))
		
		
		;
	}

    @Test 
    public void deveFazerUploadEnviaArquivo() {
    	
    	given()
    	.log().all()
    	.multiPart("arquivo", new File("src/main/resources/users.pdf"))
    	.when()
    	.post("http://restapi.wcaquino.me/upload")
    	.then()
    	.log().all()
    	.statusCode(200)
    	.body("name", is("users.pdf"))
    	
    	;
    }

    @Test 
    public void NaoDeveFazerUploadArquivoGrande() {
    	
    	given()
    	.log().all()
    	.multiPart("arquivo", new File("src/main/resources/itext-2.1.0.jar"))
    	.when()
    	.post("http://restapi.wcaquino.me/upload")
    	.then()
    	.log().all()
        .time(lessThan(10000L))
    	.statusCode(413)
    	
    	;
    }

    
    @Test 
    public void deveBaixaArquivo() throws IOException{
    	byte[] image = given()
    	.log().all()
    	.when()
    	.get("http://restapi.wcaquino.me/download")
    	.then()
    	.statusCode(200)
    	.extract().asByteArray()
    	
    	;
    	
    	File imagem = new File("src/main/resources/file.jpg");
    	java.io.OutputStream out = new FileOutputStream(imagem);
    	
    	out.write(image);
    	out.close();

    Assert.assertThat(imagem.length(), lessThan(100000L));
    
    
    }
	
	
}
