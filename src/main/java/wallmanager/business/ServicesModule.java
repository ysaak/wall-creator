package wallmanager.business;

import com.google.inject.AbstractModule;

import wallmanager.business.configuration.ConfigurationManager;
import wallmanager.business.configuration.ConfigurationService;
import wallmanager.business.dao.ConfigurationDao;
import wallmanager.business.dao.ProfileDao;
import wallmanager.business.dao.yaml.ConfigurationDaoYamlImpl;
import wallmanager.business.dao.yaml.ProfileDaoYamlImpl;
import wallmanager.business.event.EventManager;
import wallmanager.business.event.EventService;
import wallmanager.business.platform.PlatformService;
import wallmanager.business.platform.PlatformServiceProvider;
import wallmanager.business.profile.ProfileManager;
import wallmanager.business.profile.ProfileService;
import wallmanager.business.worker.WorkerManager;
import wallmanager.business.worker.WorkerService;

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
		
		bind(PlatformService.class).toProvider(PlatformServiceProvider.class);
	}
	
}
