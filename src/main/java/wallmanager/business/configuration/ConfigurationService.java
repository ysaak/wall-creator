package wallmanager.business.configuration;

import wallmanager.beans.Configuration;

public interface ConfigurationService {
	/**
	 * Load stored configuration
	 */
	void load();
	
	/**
	 * Returns configuration
	 * @return Current configuration of the application
	 */
	Configuration get();
	
	/**
	 * Sotre the configuration
	 * @param config Configuration to store
	 * @throws ConfigurationException
	 */
	void store(Configuration config) throws ConfigurationException;
}
