package it.dariofabbri.ivncr.service.local;

import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.security.SecurityService;

import org.junit.Test;

public class SecurityServiceTest extends BaseServiceTest {
	
	@Test
	public void test() {
		
		SecurityService ss = ServiceFactory.createSecurityService();
		
		User user = ss.getByUsername("admin");
		System.out.println(user);
	}
}
