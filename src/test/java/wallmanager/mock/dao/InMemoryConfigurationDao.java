package wallmanager.mock.dao;

import wallmanager.beans.Configuration;
import wallmanager.business.dao.ConfigurationDao;
import wallmanager.business.exception.NoDataFoundException;

public class InMemoryConfigurationDao implements ConfigurationDao {

	private Configuration storedConfiguration = null;
	
	@Override
	public Configuration get() throws NoDataFoundException {
		if (storedConfiguration == null)
			throw new NoDataFoundException("No config stored");
		return storedConfiguration;
	}

	@Override
	public void store(Configuration data) {
		storedConfiguration = data;
	}

}
