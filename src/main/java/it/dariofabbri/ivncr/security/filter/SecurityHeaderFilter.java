package it.dariofabbri.ivncr.security.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class SecurityHeaderFilter implements Filter {

    static class FilteredRequest extends HttpServletRequestWrapper {

    	protected static final String SECURITY_TOKEN_HEADER = "X-Security-Token";
    	protected static final String SECURITY_TOKEN_PARAMETER = "x-security-token";

    	public FilteredRequest(ServletRequest request) {
    		super((HttpServletRequest)request);
    	}
    	
    	@Override
    	public String getHeader(String name) {
    		
    		if(name.equals(SECURITY_TOKEN_HEADER)) {
    		
    			String token = super.getHeader(SECURITY_TOKEN_HEADER);
    			if(token != null)
    				return token;
    			
    			// If not found in the header, look for a query argument (workaround
    			// for situations where the header is not viable, i.e. when downloading
    			// a PDF report.
    			//
   				token = super.getParameter(SECURITY_TOKEN_PARAMETER);
   				return token;
    		}
    		
    		return super.getHeader(name);
    	}
    	
    	@Override
    	public String getParameter(String name) {
    	
    		System.out.println(">>>>>>>>>>>>>getParameter");
    		
    		if(super.getHeader(SECURITY_TOKEN_HEADER) != null)
    			return super.getParameter(name);
    		
    		if(name.equals(SECURITY_TOKEN_PARAMETER))
    			return null;
    		else
    			return super.getParameter(name);
    	}
    	
    	@Override
    	public String[] getParameterValues(String name) {
    		
    		System.out.println(">>>>>>>>>>>>>getParameterValues");

    		if(super.getHeader(SECURITY_TOKEN_HEADER) != null)
    			return super.getParameterValues(name);
    		
    		if(name.equals(SECURITY_TOKEN_PARAMETER))
    			return null;
    		else
    			return super.getParameterValues(name);
    	}
    	
    	@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
    	public Map getParameterMap() {
    		
    		System.out.println(">>>>>>>>>>>>>getParameterMap");
    		
    		if(super.getHeader(SECURITY_TOKEN_HEADER) != null)
    			return super.getParameterMap();
    		
    		Map map = super.getParameterMap();
    		Map copy = new HashMap();
    		for(Object key : map.keySet()) {
    			
    			if(key.equals(SECURITY_TOKEN_PARAMETER))
    				continue;
    			
    			copy.put(key, map.get(key));
    		}
    		
    		return copy;
    	}
    	
    	@SuppressWarnings("rawtypes")
		@Override
    	public Enumeration getParameterNames() {
    		throw new UnsupportedOperationException();
    	}
    	
    	@Override
    	public String getQueryString() {
    		
    		System.out.println(">>>>>>>>>>>>>getQueryString");

    		String qs = super.getQueryString();
    		if(qs == null)
    			return null;
    		
    		if(super.getHeader(SECURITY_TOKEN_HEADER) != null)
    			return qs;
    		
    		StringBuffer sb = new StringBuffer();
    		
    		String[] parts = qs.split("&");
    		boolean first = true;
    		
    		for(String part : parts) {
    			
    			if(part.startsWith(SECURITY_TOKEN_PARAMETER))
    				continue;
    			
    			if(first) {
    				first = false;
    			}
    			else {
    				sb.append("&");
    			}
    			
    			sb.append(part);
    		}
    		
    		String s = sb.toString();
    		return s.length() == 0 ? null : s;
    	}
    }

    public void doFilter(ServletRequest request, ServletResponse response,
    		FilterChain chain) throws IOException, ServletException {
    	chain.doFilter(new FilteredRequest(request), response);
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
    }
}
