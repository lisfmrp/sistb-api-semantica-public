package br.usp.websemantica.layer.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import br.usp.websemantica.config.HandleKeystore;
import br.usp.websemantica.config.HandleProperties;

public class CryptRSA {
		
	/*public static PublicKey getRSAPublicKey(String target)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException, UnrecoverableEntryException {
		
		HandleKeystore hk = new HandleKeystore();
		HandleProperties hp = new HandleProperties(); 
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		
		char[] keyStorePassword = hp.getContent("config.keystore.passwd").toCharArray();
		try(InputStream keyStoreData = hk.getKeystoreFile()){
		    keyStore.load(keyStoreData, keyStorePassword);
		}
		
		Certificate cert = keyStore.getCertificate(target);
		return cert.getPublicKey();
	}*/
	
	public static PrivateKey getRSAPrivateKey(String target)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, KeyStoreException, CertificateException, UnrecoverableEntryException {
		
		HandleKeystore hk = new HandleKeystore();
		HandleProperties hp = new HandleProperties(); 
		
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		
		char[] keyStorePassword = hp.getContent("config.keystore.passwd").toCharArray();
		try(InputStream keyStoreData = hk.getKeystoreFile()) {
		    keyStore.load(keyStoreData, keyStorePassword);
		}
		
		char[] keyPassword = hp.getContent("config.keystore.sistb.key.passwd").toCharArray();
		KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(keyPassword);
		
		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(target, entryPassword);
		return privateKeyEntry.getPrivateKey();
	}
	
	public static byte[] encrypt(Key key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(plaintext);
	}

	public static byte[] decrypt(Key key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(ciphertext);
	}
	
	public static boolean verifySignature(String text, String signature, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {		
		Signature sign = Signature.getInstance("SHA256withRSA");
		sign.initVerify(publicKey);
        sign.update(text.getBytes());
        return sign.verify(Base64.decodeBase64(signature));
	}
	
	public static String sign(String text, PrivateKey privateKey) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException {
		Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(text.getBytes());
        return new String(Base64.encodeBase64(sign.sign()), "UTF-8");            
	}
		
	public static PublicKey getRSAPublicKeyPEM(String keystr) {					
		try {
			keystr = keystr.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
			KeyFactory  kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.decodeBase64(keystr)); 
		    PublicKey pubKey = kf.generatePublic(keySpecX509);	      	    
		    return pubKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public static boolean isValidKeySize(PublicKey pubKey) {
		try {
			if(Integer.parseInt(pubKey.toString().split("\n")[0].split(" ")[4]) < 2048) {
				return false;
			}
		} catch(Exception e) {
			return false;
		}
		return true;
	}
}
