package br.usp.websemantica.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;

@WebFilter(urlPatterns = { "/*" }, description = "Log4j Filter add username")
public class Log4JFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		try {
            HttpSession session = req.getSession(false);
			 if (session != null) {
				 MDC.put("username", session.getAttribute("identificacaoUsuario"));
			 }
			
		} catch(NullPointerException e){
			
		} finally {
			chain.doFilter(request, response);
			MDC.remove("username");
			MDC.clear();
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
