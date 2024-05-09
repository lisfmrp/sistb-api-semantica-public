package br.usp.websemantica.utils;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class Test {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {		
	      createRsaPublicKey(
	    	        "-----BEGIN RSA PUBLIC KEY-----\n" +
	    	        "MIGHAoGBANAahj75ZIz9nXqW2H83nGcUao4wNyYZ9Z1kiNTUYQl7ob/RBmDzs5rY\n" +
	    	        "mUahXAg0qyS7+a55eU/csShf5ATGzAXv+DDPcz8HrSTcHMEFpuyYooX6PrIZ07Ma\n" +
	    	        "XtsJ2J4mhlySI5uOZVRDoaFY53MPQx5gud2quDz759IN/0gnDEEVAgED\n" +
	    	        "-----END RSA PUBLIC KEY-----"
	    	        );
	}
	
	static PublicKey createRsaPublicKey(String keystr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	   try (StringReader reader = new StringReader(keystr);
	           PemReader pemReader = new PemReader(reader))
	   {
	       PemObject pem = pemReader.readPemObject();
	       byte[] der = pem.getContent();
	       ASN1InputStream in = new ASN1InputStream(der);
	       ASN1Primitive primitive = in.readObject();
	       RSAPublicKey key = RSAPublicKey.getInstance(primitive);
	       RSAPublicKeySpec spec = new RSAPublicKeySpec(
	               key.getModulus(), key.getPublicExponent()
	       );
	       KeyFactory factory = KeyFactory.getInstance("RSA");
	       return factory.generatePublic(spec);
	   }
	}
	   
}

