package it.dariofabbri.ivncr.security.filter;

import it.dariofabbri.ivncr.service.rest.resource.SecurityResource;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityResource.class);

	protected static final String SECURITY_TOKEN_HEADER = "X-Security-Token";
	
	private Pattern excludePattern = null;
	private boolean autoLogonMode = false;
	private String autoLogonUser = null;
	private String autoLogonPassword = null;
	
	@Override
	public void init(FilterConfig filterConfig) 
		throws ServletException {

		String exclude = filterConfig.getInitParameter("exclude");
		if(exclude != null) {
			excludePattern = Pattern.compile(exclude);
		}

		autoLogonUser = filterConfig.getInitParameter("autoLogonUser");
		autoLogonPassword = filterConfig.getInitParameter("autoLogonPassword");
		String s = filterConfig.getInitParameter("autoLogonMode");
		if(s != null)
			autoLogonMode = Boolean.parseBoolean(s);
	}

	@Override
	public void doFilter(
			ServletRequest request, 
			ServletResponse response,
			FilterChain chain) 
		throws IOException, ServletException {

		// Check request type.
		//
		if(!(request instanceof HttpServletRequest)) {
			raiseSecurityError(response, "Unexpected request class detected.");
			return;
		}
		HttpServletRequest hsr = (HttpServletRequest)request;
		
		// Try to match URL against exclude pattern.
		//
		String requestUri = hsr.getRequestURI();
		if(excludePattern != null && excludePattern.matcher(requestUri).matches()) {
			chain.doFilter(request, response);
			return;
		}

		Subject requestSubject = null;
		
		// Check if autologon mode is on.
		//
		if(autoLogonMode) {

			requestSubject = new Subject.Builder().buildSubject();

			UsernamePasswordToken upt = new UsernamePasswordToken(
					autoLogonUser,
					autoLogonPassword);
			
			try {
				requestSubject.login(upt);
				
			} catch(AuthenticationException ae) {

				logger.info(ae.getMessage());
				raiseSecurityError(response, "Unable to logon. Cause: " + ae.getMessage());
				return;
			}

		} else {
			
			// Extract security token from custom header.
			//
			String token = hsr.getHeader(SECURITY_TOKEN_HEADER);
			logger.debug("Security token found in header: " + token);
			
			// SecurityResource token must be present.
			//
			if(token == null) {
				logger.info("No token found in request headers.");
				raiseSecurityError(response, "Null token detected");
				return;
			}
			
			// Check if the passed token is associated with a valid session.
			//
			try {
				Session session = SecurityUtils.getSecurityManager().getSession(new DefaultSessionKey(token));
				if(session == null) {
					logger.info("Unable to find a valid session using token.");
					raiseSecurityError(response, "Invalid session token.");
					return;
				}
				
				// Ensure that the last accessed timestamp gets updated. This should be made
				// internally by Shiro, but it looks like it is not working. Maybe the pattern
				// employed here to get the session is not canonical...
				//
				session.touch();
				
			} catch(SessionException se) {
				
				logger.info(se.getMessage());
				raiseSecurityError(response, "Invalid session token. Cause: " + se.getMessage());
				return;
			}
			
			requestSubject = new Subject.Builder().sessionId(token).buildSubject();
		}
		
		ThreadState threadState = new SubjectThreadState(requestSubject);
		threadState.bind();
		try {
			chain.doFilter(request, response);
		}
		finally {
			threadState.clear();
		}
	}

	private void raiseSecurityError(ServletResponse response, String message) {

		try {
			HttpServletResponse hsr = (HttpServletResponse)response;
			hsr.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			OutputStreamWriter osw = new OutputStreamWriter(hsr.getOutputStream());
			osw.write(message);
			osw.flush();
		} catch(Exception e) {
			logger.error("Exception caught while returning security error.", e);
		}
	}

	@Override
	public void destroy() {
		
	}

}
