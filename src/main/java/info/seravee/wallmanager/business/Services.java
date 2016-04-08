package info.seravee.wallmanager.business;

import java.util.HashMap;
import java.util.Map;

import info.seravee.wallmanager.business.configuration.ConfigurationManager;
import info.seravee.wallmanager.business.configuration.ConfigurationService;
import info.seravee.wallmanager.business.events.EventManager;
import info.seravee.wallmanager.business.events.EventService;
import info.seravee.wallmanager.business.profiles.ProfileManager;
import info.seravee.wallmanager.business.profiles.ProfileService;

public final class Services {
	public static EventService getEventService() {
		EventService service = getInstance(EventService.class);
		
		if (service == null) {
			service = new EventManager();
			putInstance(EventService.class, service);
		}
		
		return service;
	}
	
	public static ProfileService getProfileService() {
		ProfileService service = getInstance(ProfileService.class);
		
		if (service == null) {
			service = new ProfileManager();
			putInstance(ProfileService.class, service);
		}
		
		return service;
	}
	
	public static ConfigurationService getConfigurationService() {
		ConfigurationService service = getInstance(ConfigurationService.class);
		
		if (service == null) {
			service = new ConfigurationManager();
			putInstance(ConfigurationService.class, service);
		}
		
		return service;
	}
	
	/* ----------- */
	private static final Map<Class<?>, Object> instances = new HashMap<>();
		
	private static <T> void putInstance(Class<T> type, T instance) {
		instances.put(type, instance);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getInstance(Class<T> type) {
		Object o = instances.get(type);
		if (o == null) {
			return null;
		}
		return (T) o;
	}
}
