package it.dariofabbri.ivncr.service.local;

import it.dariofabbri.ivncr.service.local.contact.ContactService;
import it.dariofabbri.ivncr.service.local.contact.ContactServiceImpl;
import it.dariofabbri.ivncr.service.local.security.SecurityService;
import it.dariofabbri.ivncr.service.local.security.SecurityServiceImpl;

public class ServiceFactory {
	
	public static SecurityService createSecurityService() {
		
		SecurityService service = SessionDecorator.<SecurityService>createProxy(new SecurityServiceImpl(), SecurityService.class);
		return service;
	}
	
	public static ContactService createContactService() {
		
		ContactService service = SessionDecorator.<ContactService>createProxy(new ContactServiceImpl(), ContactService.class);
		return service;
	}

}
