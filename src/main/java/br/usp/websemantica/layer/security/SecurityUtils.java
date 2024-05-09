package br.usp.websemantica.layer.security;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import br.usp.websemantica.model.TokenSisTB;

public class SecurityUtils {
	
	static final Logger log = Logger.getLogger(SecurityUtils.class);
	
	public static String generateHashMD5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(),0,s.length());
		String md5 = new BigInteger(1,m.digest()).toString(16);
		return md5;
	}
	
	public static TokenSisTB generateSisTbToken() throws KeyStoreException, CertificateException, UnrecoverableEntryException {
		TokenSisTB tk = new TokenSisTB();
		try {
			String token = RandomStringUtils.randomAlphanumeric(32).toUpperCase().trim();
			byte[] tokenBytes = token.getBytes("UTF-8");
			byte[] base64encodedToken = Base64.encodeBase64(CryptRSA.encrypt(CryptRSA.getRSAPrivateKey("sistb-api"), tokenBytes));

			tk.setToken(token);
			tk.setTokenBase64(new String(base64encodedToken, "UTF-8"));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | InvalidKeySpecException | IOException e) {
			log.error("Erro ao gerar SISTB Token");
			tk.setToken("error");
			e.printStackTrace();
		}
		return tk;
	}
	
}
