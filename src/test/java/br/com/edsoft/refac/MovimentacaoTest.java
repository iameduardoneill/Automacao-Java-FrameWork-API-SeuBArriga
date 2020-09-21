package br.com.edsoft.refac;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.edsoft.rest.core.BaseTest;
import br.com.edsoft.rest.utils.BarrigaUtils;
import br.com.edsoft.rest.utils.DataUtils;
import br.com.edsoft.teste.Movimentacao;
import io.restassured.RestAssured;

public class MovimentacaoTest extends BaseTest{
	
	@Test
	public void deveInserirMovimentacaoSucesso() {
		Movimentacao mov = getMovimentacaoValido();
		
	    given()
		.body(mov)
		.when()
		.post("/transacoes")
		.then()
		.statusCode(201)
		;
	}	
	
	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {
		given()
		.body("{}")
		.when()
		.post("/transacoes")
		.then()
		.statusCode(400)
		.body("$",hasSize(8))
		.body("msg",hasItems(
				"Data da Movimenta��o � obrigat�rio",
				"Data do pagamento � obrigat�rio",
			    "Descri��o � obrigat�rio",
			    "Interessado � obrigat�rio",
		        "Valor � obrigat�rio",
		        "Valor deve ser um n�mero",
                "Conta � obrigat�rio",
                "Situa��o � obrigat�rio"
				))
		
		;
	}
	
	@Test
	public void naoDeveInserirMovimentacaoComDataFutura() {
		Movimentacao mov = getMovimentacaoValido();
        mov.setData_transacao(DataUtils.getDataDiferenteDias(2));
		
		given()
		.body(mov)
		.when()
		.post("/transacoes")
		.then()
		.statusCode(400)
         .body("$", hasSize(1))
	    .body("msg", hasItem("Data da Movimenta��o deve ser menor ou igual � data atual"))
	     
	     ;

	}	
	
	@Test
	public void naoDeveRemoverContaCOmMovimentacao() {
 		Integer CONTA_ID= BarrigaUtils.getIdContaPeloNome("Conta com movimentacao");
		
		given()
		.pathParam("id", CONTA_ID)
		.when()
		.delete("/contas/{id}")
		.then()
		.statusCode(500)
	.body("constraint", is("transacoes_conta_id_foreign"))
		;
	}	
	
	@Test
	public void deveRemoveMovimentacao() {
     Integer MOV_ID = BarrigaUtils.getIdMovPeloDescricao("Movimentacao para exclusao");
		
		given()
		.pathParam("id", MOV_ID)
		.when()
		.delete("/transacoes/{id}")
		.then()
		.statusCode(204)
		;
		
	}
	
	
	
	public Movimentacao getMovimentacaoValido() {
		Movimentacao mov = new Movimentacao();
		mov.setConta_id(BarrigaUtils.getIdContaPeloNome("Conta para movimentacoes"));
//		mov.setUsuario_id(usuario_id);
		mov.setDescricao("Descricao da Movimentacao");
		mov.setDescricao("Descricao da Movimentacao");
        mov.setEnvolvido("Envolvido na mov");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferenteDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferenteDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		return mov;
	}
	
}
