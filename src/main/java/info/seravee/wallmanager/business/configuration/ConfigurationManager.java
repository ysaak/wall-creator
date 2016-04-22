package info.seravee.wallmanager.business.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.business.dao.ConfigurationDao;
import info.seravee.wallmanager.business.exception.NoDataFoundException;

@Singleton
public class ConfigurationManager implements ConfigurationService {

	private Configuration currentConfig = new Configuration();
	
	@Inject
	private ConfigurationDao configurationDao;
	
	@Override
	public final void load() throws ConfigurationException {
		try {
			currentConfig = configurationDao.get();
		} catch (NoDataFoundException e) {
			currentConfig = new Configuration();
		}
	}
	
	@Override
	public final Configuration get() {
		return currentConfig;
	}
	
	@Override
	public final void store(Configuration config) throws ConfigurationException {
		currentConfig = config;
		configurationDao.store(currentConfig);
	}
}
