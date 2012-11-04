package it.dariofabbri.ivncr.service.local;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class SimpleTest {

	@Test
	public void test() {
		
		SecureRandomNumberGenerator srng = new SecureRandomNumberGenerator();
		String salt = srng.nextBytes(64).toHex();
		System.out.println(salt);
		
		String password = "admin";
		
		int iterations = 50000;
		
		String hashed = new SimpleHash("SHA-512", password, salt, iterations).toHex();
		System.out.println(hashed);		
	}
}
