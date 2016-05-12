package wallmanager.business.event;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;

@Singleton
public class EventManager implements EventService {
	private final Multimap<EventBusLine, Object> subscribers;
	private final Map<EventBusLine, EventBus> eventBuses;

	public EventManager() {
		subscribers = ArrayListMultimap.create();
		eventBuses = new HashMap<>();
	}

	public void register(EventBusLine line, Object object) {
		synchronized (subscribers) {
			getBus(line).register(object);
			subscribers.put(line, object);
		}
	}

	public void unregister(EventBusLine line, Object object) {
		synchronized (subscribers) {
			getBus(line).unregister(object);
			subscribers.remove(line, object);
		}
	}
	
	public void unregisterAll(EventBusLine line) {
		synchronized (subscribers) {
			
			EventBus bus = getBus(line);
			
			for (Object subscriber : subscribers.get(line)) {
				bus.unregister(subscriber);
			}
			
			subscribers.removeAll(line);
		}
	}

	public void post(EventBusLine line, Object event) {
		synchronized (subscribers) {
			getBus(line).post(event);
		}
	}
	
	private EventBus getBus(EventBusLine line) {
		EventBus bus = eventBuses.get(line);
		if (bus == null) {
			bus = new EventBus("EventBusLine." + line.toString());
			eventBuses.put(line, bus);
		}
		
		return bus;
	}
}
