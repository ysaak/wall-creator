package wallmanager;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import info.seravee.wallmanager.business.ServicesModule;
import info.seravee.wallmanager.business.dao.ConfigurationDao;
import info.seravee.wallmanager.business.dao.ProfileDao;
import info.seravee.wallmanager.business.platform.PlatformService;
import wallmanager.mock.dao.InMemoryConfigurationDao;
import wallmanager.mock.dao.InMemoryProfileDao;
import wallmanager.mock.dao.TestPlatform;

public class TestServiceModule extends ServicesModule {

	@Override
	protected void configureDaoBinding() {
		bind(ConfigurationDao.class).to(InMemoryConfigurationDao.class).in(Singleton.class);
		bind(ProfileDao.class).to(InMemoryProfileDao.class).in(Singleton.class);
	}
	
	@Override
	@Provides
	protected PlatformService providePlatformService() {
		return new TestPlatform();
	}
}