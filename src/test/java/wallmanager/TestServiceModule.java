package wallmanager;
import com.google.inject.Singleton;

import wallmanager.business.ServicesModule;
import wallmanager.business.dao.ConfigurationDao;
import wallmanager.business.dao.ProfileDao;
import wallmanager.business.platform.PlatformService;
import wallmanager.mock.dao.InMemoryConfigurationDao;
import wallmanager.mock.dao.InMemoryProfileDao;
import wallmanager.mock.dao.TestPlatformServiceProvider;

public class TestServiceModule extends ServicesModule {

	@Override
	protected void configureDaoBinding() {
		bind(ConfigurationDao.class).to(InMemoryConfigurationDao.class).in(Singleton.class);
		bind(ProfileDao.class).to(InMemoryProfileDao.class).in(Singleton.class);
		
		bind(PlatformService.class).toProvider(TestPlatformServiceProvider.class).in(Singleton.class);
	}
}
