package it.dariofabbri.ivncr.service.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class Application extends PackagesResourceConfig {

	public Application() {
		super("it.dariofabbri.ivncr.service.rest.resource");
		
		Factory<org.apache.shiro.mgt.SecurityManager> factory = 
				new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}
	
}
