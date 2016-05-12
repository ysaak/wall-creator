package wallmanager.services;

import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import wallmanager.TestServiceModule;
import wallmanager.beans.Configuration;
import wallmanager.business.configuration.ConfigurationService;

@Guice(modules = TestServiceModule.class)
public class TestConfigurationService {
	
	private final ConfigurationService service;

	@Inject
	public TestConfigurationService(ConfigurationService service) {
		this.service = service;
	}

	@Test
	public void testShouldNotReturnNull() {
		Configuration data = service.get();
		
		Assert.assertNotNull(data);
	}
}
