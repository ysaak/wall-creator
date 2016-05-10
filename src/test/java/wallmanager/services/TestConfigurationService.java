package wallmanager.services;

import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.business.configuration.ConfigurationService;
import wallmanager.TestServiceModule;

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
