package it.dariofabbri.ivncr.service.local;

import it.dariofabbri.ivncr.util.HibernateUtil;

import java.net.URL;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;

public abstract class BaseServiceTest {

	@Before
	public void setup() {

		Configuration configuration = new Configuration();
		URL url = this.getClass().getResource("/test-hibernate.cfg.xml");
    	configuration.configure(url);
    	ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry(); 
    	SessionFactory factory = configuration.buildSessionFactory(serviceRegistry);
	
    	HibernateUtil.setSessionFactory(factory);
	}
}
