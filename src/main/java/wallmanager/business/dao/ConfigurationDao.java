package wallmanager.business.dao;

import wallmanager.beans.Configuration;
import wallmanager.business.exception.NoDataFoundException;

public interface ConfigurationDao {
	Configuration get() throws NoDataFoundException;
	void store(Configuration data);
}
