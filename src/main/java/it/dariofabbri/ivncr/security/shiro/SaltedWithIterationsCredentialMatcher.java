package it.dariofabbri.ivncr.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;

public class SaltedWithIterationsCredentialMatcher implements CredentialsMatcher {

	private String hashAlgorithmName;
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token,
			AuthenticationInfo info) {

		if(!(info instanceof SaltedWithIterationAuthenticationInfo))
			throw new AuthenticationException("Unexpected authentication info class.");
		
		SaltedWithIterationAuthenticationInfo swiai = (SaltedWithIterationAuthenticationInfo)info;
		
		// Digest passed password.
		//
		String hashed = new SimpleHash(
				hashAlgorithmName, 
				token.getCredentials(), 
				swiai.getSalt(), 
				swiai.getIterations()).toHex();
		
		// Compare digested passed password with stored digest.
		//
		return hashed.equals(info.getCredentials());
	}

	public String getHashAlgorithmName() {
		return hashAlgorithmName;
	}

	public void setHashAlgorithmName(String hashAlgorithmName) {
		this.hashAlgorithmName = hashAlgorithmName;
	}
}
