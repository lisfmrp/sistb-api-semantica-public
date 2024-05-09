package br.usp.websemantica.config;

import java.io.IOException;
import java.io.InputStream;

public class HandleKeystore {  
    
    public HandleKeystore() {}  
      
    public InputStream getKeystoreFile() throws IOException {  
    	return getClass().getClassLoader().getResourceAsStream("keystore.ks");        
    }   
    
    public InputStream getPublicKeyFile(String filename) throws IOException {  
    	return getClass().getClassLoader().getResourceAsStream(filename);        
    } 
}
