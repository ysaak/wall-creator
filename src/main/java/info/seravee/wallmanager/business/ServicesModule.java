package info.seravee.wallmanager.business;

import com.google.inject.AbstractModule;

import info.seravee.wallmanager.business.configuration.ConfigurationManager;
import info.seravee.wallmanager.business.configuration.ConfigurationService;
import info.seravee.wallmanager.business.events.EventManager;
import info.seravee.wallmanager.business.events.EventService;
import info.seravee.wallmanager.business.profiles.ProfileManager;
import info.seravee.wallmanager.business.profiles.ProfileService;
import info.seravee.wallmanager.business.worker.WorkerManager;
import info.seravee.wallmanager.business.worker.WorkerService;

public class ServicesModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ConfigurationService.class).to(ConfigurationManager.class);
		bind(EventService.class).to(EventManager.class);
		bind(ProfileService.class).to(ProfileManager.class);
		bind(WorkerService.class).to(WorkerManager.class);
	}
}
