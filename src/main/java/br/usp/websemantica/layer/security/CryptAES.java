package br.usp.websemantica.layer.security;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptAES {
	    
	public static byte[] encrypt(String conteudo, byte[] chave, String initVector) throws Exception {
        Cipher encripta = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(chave, "AES");
        encripta.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(initVector.getBytes("UTF-8")));
        return encripta.doFinal(conteudo.getBytes("UTF-8"));
	}

	public static String decrypt(byte[] conteudo, byte[] chave, String initVector) throws Exception{	
        Cipher decripta = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(chave, "AES");
        decripta.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(initVector.getBytes("UTF-8")));
        return new String(decripta.doFinal(conteudo),"UTF-8");
	}
	
	public static byte[] generateSimetricKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}

}
