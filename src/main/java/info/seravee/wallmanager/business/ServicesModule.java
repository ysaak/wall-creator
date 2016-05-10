package info.seravee.wallmanager.business;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import info.seravee.wallmanager.business.configuration.ConfigurationManager;
import info.seravee.wallmanager.business.configuration.ConfigurationService;
import info.seravee.wallmanager.business.dao.ConfigurationDao;
import info.seravee.wallmanager.business.dao.ProfileDao;
import info.seravee.wallmanager.business.dao.yaml.ConfigurationDaoYamlImpl;
import info.seravee.wallmanager.business.dao.yaml.ProfileDaoYamlImpl;
import info.seravee.wallmanager.business.events.EventManager;
import info.seravee.wallmanager.business.events.EventService;
import info.seravee.wallmanager.business.platform.PlatformService;
import info.seravee.wallmanager.business.platform.WindowsPlatform;
import info.seravee.wallmanager.business.profiles.ProfileManager;
import info.seravee.wallmanager.business.profiles.ProfileService;
import info.seravee.wallmanager.business.worker.WorkerManager;
import info.seravee.wallmanager.business.worker.WorkerService;

public class ServicesModule extends AbstractModule {

	@Override
	protected void configure() {
		configureDaoBinding();
		
		bind(ConfigurationService.class).to(ConfigurationManager.class);
		bind(EventService.class).to(EventManager.class);
		bind(ProfileService.class).to(ProfileManager.class);
		bind(WorkerService.class).to(WorkerManager.class);
	}
	
	protected void configureDaoBinding() {
		bind(ConfigurationDao.class).to(ConfigurationDaoYamlImpl.class);
		bind(ProfileDao.class).to(ProfileDaoYamlImpl.class);
	}
	
	@Provides
	protected PlatformService providePlatformService() {
		return new WindowsPlatform();
	}
}
