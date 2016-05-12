package wallmanager.business.event;

import com.google.common.eventbus.DeadEvent;

public interface EventService {

	/**
	 * Registers all subscriber methods on {@code object} to receive events.
	 *
	 * @param line
	 * @param object
	 *            object whose subscriber methods should be registered.
	 */
	void register(EventBusLine line, Object object);

	/**
	 * Unregisters all subscriber methods on a registered {@code object}.
	 *
	 * @param object
	 *            object whose subscriber methods should be unregistered.
	 * @throws IllegalArgumentException
	 *             if the object was not previously registered.
	 */
	void unregister(EventBusLine line, Object object);

	/**
	 * Unregisters all subscriber methods on a registered {@code object}.
	 *
	 * @param object
	 *            object whose subscriber methods should be unregistered.
	 * @throws IllegalArgumentException
	 *             if the object was not previously registered.
	 */
	void unregisterAll(EventBusLine line);

	/**
	 * Posts an event to all registered subscribers. This method will return
	 * successfully after the event has been posted to all subscribers, and
	 * regardless of any exceptions thrown by subscribers.
	 *
	 * <p>
	 * If no subscribers have been subscribed for {@code event}'s class, and
	 * {@code event} is not already a {@link DeadEvent}, it will be wrapped in a
	 * DeadEvent and reposted.
	 *
	 * @param event
	 *            event to post.
	 */
	void post(EventBusLine line, Object event);
}
