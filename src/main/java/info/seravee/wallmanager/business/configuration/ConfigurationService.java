package info.seravee.wallmanager.business.configuration;

import info.seravee.wallmanager.beans.Configuration;

public interface ConfigurationService {
	/**
	 * Load stored configuration
	 * @throws ConfigurationException
	 */
	void load() throws ConfigurationException;
	
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