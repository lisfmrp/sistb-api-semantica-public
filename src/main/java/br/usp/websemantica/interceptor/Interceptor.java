package br.usp.websemantica.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Interceptor extends HandlerInterceptorAdapter{
	
	static final Logger log = Logger.getLogger(Interceptor.class);
	
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object controller) throws IOException{
		String uri = req.getRequestURI();		
		if(!uri.contains("/resources/"))
			log.info(req.getMethod() + " " + uri);
		return true;							
	}
}