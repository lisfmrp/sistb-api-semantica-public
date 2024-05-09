package br.usp.websemantica.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HandleProperties {  
    
    private Properties props;

    public HandleProperties() {       
    	InputStream in = getClass().getClassLoader().getResourceAsStream("config.dev.properties");
        try {  
            loadProperties(in);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
      
    private void loadProperties(InputStream in) throws IOException {  
        props = new Properties();  
        props.load(in);  
        in.close();  
    }  

    public String getContent(String chave) {  
        return props.getProperty(chave).trim();  
    } 
    
}
