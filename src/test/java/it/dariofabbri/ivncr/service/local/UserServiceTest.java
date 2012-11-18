package it.dariofabbri.ivncr.service.local;

import it.dariofabbri.ivncr.model.security.Role;
import it.dariofabbri.ivncr.model.security.User;
import it.dariofabbri.ivncr.service.local.user.UserService;

import org.junit.Assert;
import org.junit.Test;

public class UserServiceTest extends BaseServiceTest {
	
	@Test
	public void testCreate() {
		
		UserService us = ServiceFactory.createUserService();
		
		User user = us.createUser("gino", "password", "Gino", "Pilota", "A test user");
		Assert.assertNotNull(user);
		
		System.out.println(user.getId());
		
		Role role = us.addRoleToUser(user.getId(), 1);
		Assert.assertNotNull(role);
	}
	
	@Test
	public void testDelete() {
		
		UserService us = ServiceFactory.createUserService();
		
		User user = us.retrieveUserByUsername("gino");
		Assert.assertNotNull(user);
		
		System.out.println(user.getId());
		
		us.deleteUserById(user.getId());
	}
}
