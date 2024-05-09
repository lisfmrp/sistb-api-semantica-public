package br.usp.websemantica.layer.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.usp.websemantica.config.HandleProperties;
import br.usp.websemantica.layer.security.CryptAES;
import br.usp.websemantica.layer.security.CryptRSA;
import br.usp.websemantica.layer.security.SecurityService;
import br.usp.websemantica.layer.security.SecurityUtils;
import br.usp.websemantica.model.NivelAcessoPropriedade;
import br.usp.websemantica.model.ResponseApi;
import br.usp.websemantica.model.SistemaExterno;
import br.usp.websemantica.model.TokenSisTB;
import br.usp.websemantica.utils.Utils;

@Component
@Transactional(rollbackFor={Exception.class})	
public class ApiService {
	
static final Logger log = Logger.getLogger(ApiService.class);
		
	@Autowired
	private SecurityService securityService;
	
	public ResponseApi processSPARQL(String queryString, String formato, String apiKey, String signature, int nivelEntidade, boolean criptografar) throws Exception {
		ResponseApi response = new ResponseApi();
    	SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByApiKey(apiKey);
    	
    	String decodedSignature = new String(Base64.decodeBase64(signature));
    	boolean isValidSignature = CryptRSA.verifySignature(sistemaExterno.getApiKey(), decodedSignature, CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()));
    	
    	boolean authorized = true;
    	if(sistemaExterno == null
    			|| sistemaExterno.getNivelMax() < nivelEntidade 
    			|| nivelEntidade == 0
    			|| !isValidSignature) {
    		log.error("Acesso não autorizado - API Key");
    		authorized = false;
    	}
    	
    	if(!authorized) {
    		response.setCode(ResponseApi.UNAUTHORIZED);
			response.setMessage("Não autorizado");
			return response;
    	}
    	
		BufferedReader in = null;
    	HandleProperties hp = new HandleProperties();
    	String resultData = "";   
    	
		try {
    		String urlString = hp.getContent("config.endpoint.url.sparql") + "?query=" + URLEncoder.encode(queryString,"UTF-8") + "&output=json";
    		log.info(urlString);
    		
    		URL url = new URL (urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
                       
            InputStream content = (InputStream)connection.getInputStream();
            in = new BufferedReader (new InputStreamReader (content));	                      							
		} catch(Exception ex) {
			log.error("Erro ao executar query SPARQL");
			resultData = "";
			ex.printStackTrace();
			response.setCode(ResponseApi.ERROR);
			response.setMessage("Erro ao executar query SPARQL");
			return response;
		}
		
		try {
    		resultData = in.lines().collect(Collectors.joining());    
    		byte[] chaveSimetrica = CryptAES.generateSimetricKey();	
    		String initVector = RandomStringUtils.randomAlphanumeric(16);
    		
    		response.setInitVector(initVector);
    		//response.setObj(CryptAES.encrypt(resultData, chaveSimetrica, initVector));
    		response.setObj(resultData);
	    	response.setObjSignature(CryptRSA.sign(resultData, CryptRSA.getRSAPrivateKey("sistb-api")));
	    	response.setKey(Base64.encodeBase64String(CryptRSA.encrypt(CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()), chaveSimetrica)));
	    	response.setHashMD5(SecurityUtils.generateHashMD5(resultData));
	    	response.setHashMD5Signature(CryptRSA.sign(response.getHashMD5(), CryptRSA.getRSAPrivateKey("sistb-api")));
			response.setCode(ResponseApi.OK);	
		} catch (Exception ex) {
			log.error("Erro ao criar resposta");
			ex.printStackTrace();
			response.setCode(ResponseApi.ERROR);
			response.setMessage("Erro ao criar resposta");
			return response;
		}
		return response;
		
	}

	public ResponseApi process(String urlToExtract, String formato, String apiKey, String signature, TokenSisTB tokenSisTB, int nivelEntidade, boolean criptografar) throws Exception {
		ResponseApi response = new ResponseApi();									    	     
		SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByApiKey(apiKey);
    	
		String decodedSignature = new String(Base64.decodeBase64(signature));
    	boolean isValidSignature = CryptRSA.verifySignature(sistemaExterno.getApiKey(), decodedSignature, CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()));
		
    	if(sistemaExterno == null 
    			|| sistemaExterno.getNivelMax() < nivelEntidade 
    			|| nivelEntidade == 0
    			|| !isValidSignature) {
    		tokenSisTB.setUtilizado(1);
    		tokenSisTB.setDataUtilizacao(new Date());
    		this.securityService.updateToken(tokenSisTB);
    		
    		response.setCode(ResponseApi.UNAUTHORIZED);
			response.setMessage("Não autorizado");
			return response;
    	}
    	
		if(!Utils.isValidFormat(formato)) {    		
			response.setMessage("O formato informado é inválido! JSON-LD será utilizado como padrão.");		
			formato = "json-ld";
		}
		
		urlToExtract += "&token=" + tokenSisTB.getTokenBase64();
		System.out.println(urlToExtract);
		Model model = this.verifyStatements(Utils.extractPageDataAsModel(urlToExtract), sistemaExterno);				
		String resultData = Utils.convertModelFormat(model, formato);		

	    if(resultData != null && !resultData.equals("")) {
	    	if(resultData.contains("index.html")) {
	    		response.setCode(ResponseApi.UNAUTHORIZED);
				response.setMessage("Não autorizado");
				return response;
	    	} else {	    	
		    	log.info("Dados extraidos do SisTb");		    	
		    	
		    	if(criptografar) {			    	
		    		byte[] chaveSimetrica = CryptAES.generateSimetricKey();
		    		String initVector = RandomStringUtils.randomAlphanumeric(16);
		    		
		    		response.setInitVector(initVector);
			    	//response.setObj(CryptAES.encrypt(resultData, chaveSimetrica, initVector));
			    	response.setObjSignature(CryptRSA.sign(resultData, CryptRSA.getRSAPrivateKey("sistb-api")));
			    	response.setObj(resultData);
			    	response.setKey(Base64.encodeBase64String(CryptRSA.encrypt(CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()), chaveSimetrica)));
		    	} else {
		    		response.setObj(model);	
		    	}
		    	
		    	response.setHashMD5(SecurityUtils.generateHashMD5(resultData));
		    	response.setHashMD5Signature(CryptRSA.sign(response.getHashMD5(), CryptRSA.getRSAPrivateKey("sistb-api")));
				response.setCode(ResponseApi.OK);
	    	}
	    }			
    			
		if(response.getObj() == null && response.getCode() != ResponseApi.ERROR) {
			log.error("Objeto não encontrado");
			response.setCode(ResponseApi.NOT_FOUND);
			response.setMessage("Objeto nao encontrado");
		}
		
		tokenSisTB.setUtilizado(1);
		tokenSisTB.setDataUtilizacao(new Date());
		this.securityService.updateToken(tokenSisTB);
		
		return response;
	}
	
	private Model verifyStatements(Model model, SistemaExterno sistemaExterno) {
		List<Statement> stmtToAdd = new ArrayList<Statement>();
		List<Statement> stmtToRemove = new ArrayList<Statement>();		
		List<NivelAcessoPropriedade> listaNivelProps = this.securityService.getNivelAcessoPropriedades();
		List<String> listaPropriedadesBloqueadas = this.securityService.getPropriedadesBloqueadasBySistemaExterno(sistemaExterno.getId());
		
		StmtIterator iter = model.listStatements();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); 
			Property predicate = stmt.getPredicate();  
			
			if(stmt.getSubject().toString().startsWith("http://")) {
				stmtToRemove.add(stmt);
			} else {			
				//int nivelAcessoProp = this.securityService.getNivelMaxPropriedade(predicate.getURI());				
				for (NivelAcessoPropriedade nivelAcessoPropriedade : listaNivelProps) {
					if(nivelAcessoPropriedade.getPropriedadeUri().equals(predicate.getURI())) {
						if(nivelAcessoPropriedade.getNivel() == 0 || listaPropriedadesBloqueadas.contains(predicate.getURI()) || sistemaExterno.getNivelMax() < nivelAcessoPropriedade.getNivel()) {
							stmtToRemove.add(stmt);
							
							Property newProperty = ResourceFactory.createProperty(predicate.getURI());
							Literal newObject = ResourceFactory.createStringLiteral("NIVEL DE ACESSO INSUFICIENTE");
							Statement newStmt = ResourceFactory.createStatement(stmt.getSubject(), newProperty, newObject);
							stmtToAdd.add(newStmt);
							
							break;
						}
					}
				}
			}
		}
		model.remove(stmtToRemove);
		model.add(stmtToAdd);
		
		model.removeNsPrefix("doac");
		model.removeNsPrefix("foaf");
				
		/*iter = model.listStatements();
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement(); 			 
			System.out.println(stmt.getSubject() + " " + stmt.getPredicate() + " " + stmt.getObject());
		}*/
		
		return model;
	}


}
