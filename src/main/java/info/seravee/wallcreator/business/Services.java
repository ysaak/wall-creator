package info.seravee.wallcreator.business;

import java.util.HashMap;
import java.util.Map;

import com.google.common.eventbus.EventBus;

import info.seravee.wallcreator.business.profiles.ProfileManager;
import info.seravee.wallcreator.business.profiles.ProfileService;

public final class Services {
	
	private static final EventBus EVENT_BUS = new EventBus();
	
	public static EventBus getEventService() {
		return EVENT_BUS;
	}
	
	public static ProfileService getProfileService() {
		ProfileService service = getInstance(ProfileService.class);
		
		if (service == null) {
			service = new ProfileManager();
			putInstance(ProfileService.class, service);
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
