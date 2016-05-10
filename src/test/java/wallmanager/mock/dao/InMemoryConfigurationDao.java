package wallmanager.mock.dao;

import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.business.dao.ConfigurationDao;
import info.seravee.wallmanager.business.exception.NoDataFoundException;

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
