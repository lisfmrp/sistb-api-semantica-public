package br.usp.websemantica.layer.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.websemantica.config.HandleProperties;
import br.usp.websemantica.layer.security.CryptAES;
import br.usp.websemantica.layer.security.CryptRSA;
import br.usp.websemantica.layer.security.SecurityService;
import br.usp.websemantica.layer.security.SecurityUtils;
import br.usp.websemantica.model.ResponseApi;
import br.usp.websemantica.model.SistemaExterno;

@RestController
public class QueryEndpoint {
	
	static final Logger log = Logger.getLogger(QueryEndpoint.class);
	
	@Autowired
	private SecurityService securityService;
	
    @RequestMapping(value="/executeQuery",method={RequestMethod.POST},produces = MediaType.APPLICATION_JSON)
    public ResponseApi sparqlQuery(@RequestParam(value="query", required=true) String queryString, 
    		@RequestParam(value="apiKey", required=true) String apiKey, 
    		@RequestParam(value="signature", required=true) String signature,
    		@RequestParam(value="endpoint", required=true) String endpoint,
    		HttpServletRequest req, HttpServletResponse resp) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {    	   	    	
    	
    	ResponseApi response = new ResponseApi();
    	SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByApiKey(apiKey);
    	
    	String decodedSignature = new String(Base64.decodeBase64(signature));
    	boolean isValidSignature = CryptRSA.verifySignature(sistemaExterno.getApiKey(), decodedSignature, CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()));
    	
    	boolean authorized = true;
    	if(sistemaExterno == null || !isValidSignature) {
    		log.error("Acesso não autorizado - API Key - Query Endpoint");
    		authorized = false;
    	} else if(endpoint.equals("sparql") && sistemaExterno.getSparql() == 0) {
    		log.error("Acesso não autorizado - API Key - SPARQL Endpoint");
    		authorized = false;
    	} else if(endpoint.equals("graphql") && sistemaExterno.getGraphql() == 0) {
    		log.error("Acesso não autorizado - API Key - GraphQL Endpoint");
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
    	
    	if(endpoint.equals("sparql")) {
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
			}
    	} else if(endpoint.equals("graphql")) {
    		try {
    			JSONObject data = new JSONObject();
    			data.put("query", queryString);
    			
	    		URL url = new URL (hp.getContent("config.endpoint.url.graphql"));
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	                                    
	            connection.setRequestMethod("POST");
	            connection.setDoOutput(true);
	            connection.setRequestProperty("Content-Type", "application/json");
	            connection.setRequestProperty("Accept", "application/json");
	            connection.getOutputStream().write(data.toString().getBytes("UTF-8"));
	            
	            InputStream content = (InputStream)connection.getInputStream();
	            in = new BufferedReader (new InputStreamReader (content));	                       	                                 	           
			} catch(Exception ex) {
				log.error("Erro ao executar query GraphQL");
				resultData = "";
				ex.printStackTrace();
			}    	
    	}    	        
    	
    	try {
    		resultData = in.lines().collect(Collectors.joining());    
    		byte[] chaveSimetrica = CryptAES.generateSimetricKey();	
    		String initVector = RandomStringUtils.randomAlphanumeric(16);
    		
    		response.setInitVector(initVector);
    		response.setObj(CryptAES.encrypt(resultData, chaveSimetrica, initVector));
	    	response.setObjSignature(CryptRSA.sign(resultData, CryptRSA.getRSAPrivateKey("sistb-api")));
	    	response.setKey(Base64.encodeBase64String(CryptRSA.encrypt(CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()), chaveSimetrica)));
	    	response.setHashMD5(SecurityUtils.generateHashMD5(resultData));
	    	response.setHashMD5Signature(CryptRSA.sign(response.getHashMD5(), CryptRSA.getRSAPrivateKey("sistb-api")));
			response.setCode(ResponseApi.OK);	
		} catch (Exception ex) {
			log.error("Erro ao criar resposta");
			response = null;
			ex.printStackTrace();
		}
    	
    	return response;
    }
}
