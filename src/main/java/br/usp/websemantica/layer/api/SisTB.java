package br.usp.websemantica.layer.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.websemantica.layer.security.SecurityService;
import br.usp.websemantica.model.Paciente;
import br.usp.websemantica.model.ResponseApi;
import br.usp.websemantica.service.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/api/sistb")
@Api(value="sistb-api", description="Dados de pacientes, tratamentos, exames de controle mensal, internações e supervisões (DOT) armazenados no SISTB")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Recurso retornado com sucesso"),
        @ApiResponse(code = 401, message = "Não autorizado"),
        @ApiResponse(code = 403, message = "Acesso Proibido"),
        @ApiResponse(code = 404, message = "Recurso não encontrado")
})
public class SisTB {
	
	static final Logger log = Logger.getLogger(SisTB.class);
	
	@Autowired
	private ApiService apiService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private Service service;
	
	//private HandleProperties hp = new HandleProperties();
	
	@ApiOperation(value = "Retorna um paciente identificado pelo código interno, CPF, CNS ou Número SINAN", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/paciente/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getPacienteByNroSinan(@RequestParam(value="id", required=true) String id, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Paciente");	
		Paciente p = this.service.getPaciente(id);
		String queryString = "SELECT DISTINCT ?property ?hasValue ?isValueOf\r\n" + 
    			"WHERE {\r\n" + 
    			"  { <http://api.tuberculosis.cloud/d2rq/resource/paciente/"+p.getCodPaciente()+"> ?property ?hasValue }\r\n" + 
    			"  UNION\r\n" + 
    			"  { ?isValueOf ?property <http://api.tuberculosis.cloud/d2rq/resource/paciente/"+p.getCodPaciente()+"> }\r\n" + 
    			"}\r\n" + 
    			"ORDER BY (!BOUND(?hasValue)) ?property ?hasValue ?isValueOf";
		
		ResponseApi response = this.apiService.processSPARQL(queryString, formato, req.getParameter("api_key"), signature, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}	
	
	@ApiOperation(value = "Retorna um tratamento identificado pelo seu código interno", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/tratamento/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getTratamentoById(@RequestParam(value="codTratamento", required=true) int codTratamento, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Tratamento");	
		String queryString = "SELECT DISTINCT ?property ?hasValue\r\n" + 
				"WHERE {\r\n" + 
				"  { <http://api.tuberculosis.cloud/d2rq/resource/tratamento/"+codTratamento+"> ?property ?hasValue }\r\n" + 
				"}\r\n" + 
				"ORDER BY (!BOUND(?hasValue)) ?property ?hasValue";
		
		ResponseApi response = this.apiService.processSPARQL(queryString, formato, req.getParameter("api_key"), signature, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
	
	@ApiOperation(value = "Retorna um supervisionamento identificado pelo seu código interno", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/supervisionamento/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getSupervisionamentoById(@RequestParam(value="codSupervisionamento", required=true) int codSupervisionamento, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Supervisionamento");	
		String queryString = "SELECT DISTINCT ?property ?hasValue\r\n" + 
				"WHERE {\r\n" + 
				"  { <http://api.tuberculosis.cloud/d2rq/resource/supervisionamento/"+codSupervisionamento+"> ?property ?hasValue }\r\n" + 
				"}\r\n" + 
				"ORDER BY (!BOUND(?hasValue)) ?property ?hasValue";
		
		ResponseApi response = this.apiService.processSPARQL(queryString, formato, req.getParameter("api_key"), signature, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
	
	@ApiOperation(value = "Retorna um registro de internação identificado pelo seu código interno", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/internacao/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getInternacaoById(@RequestParam(value="codInternacao", required=true) int codInternacao, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Internação");	
		String queryString = "SELECT DISTINCT ?property ?hasValue\r\n" + 
				"WHERE {\r\n" + 
				"  { <http://api.tuberculosis.cloud/d2rq/resource/internacao/"+codInternacao+"> ?property ?hasValue }\r\n" + 
				"}\r\n" + 
				"ORDER BY (!BOUND(?hasValue)) ?property ?hasValue";
		
		ResponseApi response = this.apiService.processSPARQL(queryString, formato, req.getParameter("api_key"), signature, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
	
	@ApiOperation(value = "Retorna um registro de controle mensal (exames) identificado pelo seu código interno", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/controlemensal/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getControleMensalById(@RequestParam(value="codControle", required=true) int codControle, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Internação");	
		String queryString = "SELECT DISTINCT ?property ?hasValue\r\n" + 
				"WHERE {\r\n" + 
				"  { <http://api.tuberculosis.cloud/d2rq/resource/controle_mensal/"+codControle+"> ?property ?hasValue }\r\n" + 
				"}\r\n" + 
				"ORDER BY (!BOUND(?hasValue)) ?property ?hasValue";
		
		ResponseApi response = this.apiService.processSPARQL(queryString, formato, req.getParameter("api_key"), signature, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
	
	/*@ApiOperation(value = "Retorna um paciente identificado pelo seu código interno", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/paciente/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getPacienteById(@RequestParam(value="codPaciente", required=true) int codPaciente, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
				
		TokenSisTB tk = SecurityUtils.generateSisTbToken();
		this.securityService.saveToken(tk);
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Paciente");		
		String urlToExtract = this.hp.getContent("config.sistb.url") + "/paciente_form.php?codPaciente=" + codPaciente;    	
		
		ResponseApi response = this.apiService.process(urlToExtract, formato, req.getParameter("api_key"), signature, tk, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
		
	@ApiOperation(value = "Retorna um tratamento identificado pelo seu código interno", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/tratamento/"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getTratamentoById(@RequestParam(value="codTratamento", required=true) int codTratamento, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		TokenSisTB tk = SecurityUtils.generateSisTbToken();
		this.securityService.saveToken(tk);
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Tratamento");
		String urlToExtract =  this.hp.getContent("config.sistb.url") + "/tratamento_form.php?codTratamento=" + codTratamento;    	
		
		ResponseApi response = this.apiService.process(urlToExtract, formato, req.getParameter("api_key"), signature, tk, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
	
	@ApiOperation(value = "Retorna um tratamento identificado pelo código de um paciente", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/tratamento/findByCodPaciente"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getTratamentoByCodPaciente(@RequestParam(value="codPaciente", required=true) int codPaciente, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
						
		List<br.usp.websemantica.model.Tratamento> tratamentos = this.service.getTratamentoByCodPaciente(codPaciente);		
		ResponseApi response = this.process(formato, req.getParameter("api_key"), signature, tratamentos);		
		resp.setStatus(response.getCode());
		return response;
	}	
	
	@ApiOperation(value = "Retorna um tratamento identificado pelo Número SINAN de um paciente", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/tratamento/findByNroSinanPaciente"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getTratamentoByNroSinanPaciente(@RequestParam(value="nroSinanPaciente", required=true) String nroSinanPaciente, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		List<br.usp.websemantica.model.Tratamento> tratamentos = this.service.getTratamentoByCodPaciente(this.service.getPacienteByNroSinan(nroSinanPaciente).getCodPaciente());		
		ResponseApi response = this.process(formato, req.getParameter("api_key"), signature, tratamentos);		
		resp.setStatus(response.getCode());
		return response;
	}
	
	@ApiOperation(value = "Retorna os dados de supervisionamento do tratamento de um paciente", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/supervisionamento/findByCodTratamento"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public ResponseApi getSupervisionamentoById(@RequestParam(value="codTratamento", required=true) int codTratamento, @RequestParam(value="formato", required=true, defaultValue="json-ld") String formato,
			@RequestParam(value="signature", required=true) String signature, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		TokenSisTB tk = SecurityUtils.generateSisTbToken();
		this.securityService.saveToken(tk);
		
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Supervisionamento");
		String urlToExtract =  this.hp.getContent("config.sistb.url") + "/ficha_supervisionamento_semantic.php?codTratamento=" + codTratamento;    	
		
		ResponseApi response = this.apiService.process(urlToExtract, formato, req.getParameter("api_key"), signature, tk, nivelEntidade, true);		    	
		resp.setStatus(response.getCode());	
		return response;
	}
	
	private ResponseApi process(String formato, String apiKey, String signature, List<br.usp.websemantica.model.Tratamento> tratamentos) throws Exception {
		int nivelEntidade = this.securityService.getNivelAcessoEntidade("Tratamento");			
		
		boolean invalid = false;
		Model model = ModelFactory.createDefaultModel();			
		for (br.usp.websemantica.model.Tratamento t : tratamentos) {
			TokenSisTB tk = SecurityUtils.generateSisTbToken();
			this.securityService.saveToken(tk);
			
			String urlToExtract =  this.hp.getContent("config.sistb.url") + "/ficha_tratamento.php?codTratamento=" + t.getCodTratamento();
			ResponseApi respTemp = this.apiService.process(urlToExtract, formato, apiKey, signature, tk, nivelEntidade, false);	
			
			if(respTemp.getCode() != ResponseApi.OK) {
				invalid = true;
				break;
			} else {
				Model modelTemp = (Model) respTemp.getObj();
				model.add(modelTemp);
			}
		}				   		
		
		ResponseApi response = new ResponseApi();
		if(invalid) {
			response.setCode(ResponseApi.UNAUTHORIZED);
			response.setMessage("Não autorizado");			
		} else {
			SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByApiKey(apiKey);
			byte[] chaveSimetrica = CryptAES.generateSimetricKey();			
			String resultData = Utils.convertModelFormat(model, formato);
    		String initVector = RandomStringUtils.randomAlphanumeric(16);
    		
    		response.setInitVector(initVector);    		
			response.setObj(CryptAES.encrypt(resultData, chaveSimetrica, initVector));	
			response.setObjSignature(CryptRSA.sign(response.getObj().toString(), CryptRSA.getRSAPrivateKey("sistb-api")));
			//response.setObj(resultData);
	    	response.setKey(Base64.encodeBase64String(CryptRSA.encrypt(CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()), chaveSimetrica)));
	    	response.setHashMD5(SecurityUtils.generateHashMD5(resultData));
	    	response.setHashMD5Signature(CryptRSA.sign(response.getHashMD5(), CryptRSA.getRSAPrivateKey("sistb-api")));
			response.setCode(ResponseApi.OK);
		}
							
		return response;
	}*/
}
