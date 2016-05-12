package wallmanager.business.configuration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import wallmanager.beans.Configuration;
import wallmanager.business.dao.ConfigurationDao;
import wallmanager.business.exception.NoDataFoundException;

@Singleton
public class ConfigurationManager implements ConfigurationService {

	private Configuration currentConfig = null;
	
	@Inject
	private ConfigurationDao configurationDao;
	
	@Override
	public final void load() {
		try {
			currentConfig = configurationDao.get();
		} catch (NoDataFoundException e) {
			currentConfig = null;
		}
		
		if (currentConfig == null)
			currentConfig = new Configuration();
	}
	
	@Override
	public final Configuration get() {
		if (currentConfig == null)
			load();
		
		return currentConfig;
	}
	
	@Override
	public final void store(Configuration config) throws ConfigurationException {
		currentConfig = config;
		configurationDao.store(currentConfig);
	}
}
