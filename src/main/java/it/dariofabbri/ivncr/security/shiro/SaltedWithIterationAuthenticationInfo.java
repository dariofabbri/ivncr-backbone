package it.dariofabbri.ivncr.security.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class SaltedWithIterationAuthenticationInfo implements
		AuthenticationInfo {

	private static final long serialVersionUID = 1L;

	private PrincipalCollection principals;
	private Object credentials;
	private Object salt;
	private int iterations;
	
	public SaltedWithIterationAuthenticationInfo(
			Object principal, Object credentials, String realm) {
		
		principals = new SimplePrincipalCollection(principal, realm);
		this.credentials = credentials;
	}
	
	@Override
	public Object getCredentials() {
		return credentials;
	}

	public void setCredentials(Object credentials) {
		this.credentials = credentials;
	}

	@Override
	public PrincipalCollection getPrincipals() {
		return principals;
	}

	public void setPrincipals(PrincipalCollection principals) {
		this.principals = principals;
	}

	public Object getSalt() {
		return salt;
	}

	public void setSalt(Object salt) {
		this.salt = salt;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
}
