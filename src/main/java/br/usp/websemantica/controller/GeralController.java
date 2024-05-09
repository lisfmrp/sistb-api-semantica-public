package br.usp.websemantica.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import br.usp.websemantica.layer.security.CryptRSA;
import br.usp.websemantica.layer.security.SecurityService;
import br.usp.websemantica.model.SistemaExterno;

@Controller
public class GeralController {
	
	static final Logger log = Logger.getLogger(GeralController.class);
	
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value="/keyConfig",method=RequestMethod.GET,produces={"text/html"})
	@ResponseStatus(HttpStatus.OK)
    public ModelAndView keyConfig(@RequestParam(value="configKey", required=true) String configKey, HttpServletRequest req, HttpServletResponse resp) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {		
		ModelAndView mav = new ModelAndView("config_key.jsp");
		SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByConfigKey(configKey);
		
		if(sistemaExterno == null || configKey == null || sistemaExterno.getBloqueado() == 1) {
			return new ModelAndView("config_key.jsp?unauthorized");
		}
						
		if(sistemaExterno.getChavePublica() != null && !sistemaExterno.getChavePublica().equals("")) {
			mav.addObject("apiKeyCriptografada",Base64.encodeBase64String(CryptRSA.encrypt(CryptRSA.getRSAPublicKeyPEM(sistemaExterno.getChavePublica()), sistemaExterno.getApiKey().getBytes())));
		}
		
		sistemaExterno.setApiKey("");
		mav.addObject("sistemaExterno", sistemaExterno);
		return mav;
	}
	
	@RequestMapping(value="/keyConfig",method=RequestMethod.POST,produces={"text/html"})
	@ResponseStatus(HttpStatus.OK)
    public void keyConfigPost(@RequestParam(value="configKey", required=true) String configKey, 
    		@RequestParam(value="chavePublica", required=true) String chavePublica,
    		HttpServletRequest req, HttpServletResponse resp) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException {		
		
		SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByConfigKey(configKey);	
		PublicKey pubKey = CryptRSA.getRSAPublicKeyPEM(chavePublica.trim());
		
		if(sistemaExterno == null || sistemaExterno.getBloqueado() == 1) {
			resp.sendRedirect(req.getContextPath() + "/keyConfig");
			return;
		} else if(pubKey == null) {
			log.error("Chave inválida [" + sistemaExterno.getAlias() + "]");
			resp.sendRedirect(req.getContextPath() + "/keyConfig?configKey=" + sistemaExterno.getConfigKey() + "&error");
			return;
		} else if(!CryptRSA.isValidKeySize(pubKey)) {
			log.error("Chave inválida (< 2048 bits) [" + sistemaExterno.getAlias() + "]");
			resp.sendRedirect(req.getContextPath() + "/keyConfig?configKey=" + sistemaExterno.getConfigKey() + "&errorKeySize");	
			return;
		}
		
		try {
			Base64.encodeBase64String(CryptRSA.encrypt(pubKey, sistemaExterno.getApiKey().getBytes()));
			sistemaExterno.setChavePublica(chavePublica.trim());
			this.securityService.updateSistemaExterno(sistemaExterno);
			log.error("Chave pública registrada [" + sistemaExterno.getAlias() + "]");
			resp.sendRedirect(req.getContextPath() + "/keyConfig?configKey=" + sistemaExterno.getConfigKey());
		} catch(Exception e) {
			e.printStackTrace();
			log.error("Erro ao registrar chave pública [" + sistemaExterno.getAlias() + "]");
			resp.sendRedirect(req.getContextPath() + "/keyConfig?configKey=" + sistemaExterno.getConfigKey() + "&error");			
		}	
		return;
	}
	
	@RequestMapping(value="/query",method=RequestMethod.GET,produces={"text/html"})
	@ResponseStatus(HttpStatus.OK)
    public String sparql() {		
		return "query.jsp";
	}	
	
	@RequestMapping(value="/apiDocs",method=RequestMethod.GET,produces={"text/html"})
	@ResponseStatus(HttpStatus.OK)
    public void apiDocs(HttpServletRequest req, HttpServletResponse resp) throws IOException {					
		resp.sendRedirect(req.getContextPath() + "/swagger-ui.html#");
	}
	
	@RequestMapping(value="/signMyKeyTest",method=RequestMethod.GET,produces={"text/plain"})
	@ResponseStatus(HttpStatus.OK)
    public void signMyKeyTest(HttpServletRequest req, HttpServletResponse resp) throws IOException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException, UnrecoverableEntryException {					
		String apiKey = "vrtXi2V202LXjkxLPHDNMVvKfNVYBBNSYsHsPpQKh8aDTyS5H2miF78NDK5nbibGKv5YL3Td9sVITGuHoOrnlGWnN9nNHY4WL797cUJiB7znngmKM9NROpOhagKM0FWK";
		String signature = Base64.encodeBase64String(CryptRSA.sign(apiKey, CryptRSA.getRSAPrivateKey("test-system")).getBytes()); 
		String chavePublicaSistemaExterno = "-----BEGIN PUBLIC KEY-----\r\n" + 
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnUU2f6x5Xinl/oU5egbR\r\n" + 
				"BuiRIF0tCQYQAG2lu60Es0ekn/gX2slfZJjcyNDLYepLTvCQ3cenzN1uNyCP+aHb\r\n" + 
				"Tr7WLCnk3108MAZs5uTkXLbmXibmwQywoGRDrpxBMzkadQohIFofsR30JnwWphLx\r\n" + 
				"lXK+0EIAe/QDSrKKIqRXmxSWwcsg5F/hNtIkW4dgH3a6uCamuG+h5Yy8nv8u+GoQ\r\n" + 
				"uwbycknVHp4uR15RxJa4f6J6txcDc7rt0eoN7YYzaN/rfvS3XdZdp+M0+yo41tP0\r\n" + 
				"7uriZg9KXB5lv4s01H/K+knWerVQZz9v1Uv7mNDVAYWHxNbHZVFsdP2iQEdPQ/U5\r\n" + 
				"EwIDAQAB\r\n" + 
				"-----END PUBLIC KEY-----";

		System.out.println("SIGNATURE -> " + signature);
		String decodedSignature = new String(Base64.decodeBase64(signature));
		System.out.println("SIGNATURE BASE64 DECODED: " + decodedSignature);
		boolean isValidSignature = CryptRSA.verifySignature(apiKey, decodedSignature, CryptRSA.getRSAPublicKeyPEM(chavePublicaSistemaExterno));
		System.out.println(isValidSignature);		
	}	
}