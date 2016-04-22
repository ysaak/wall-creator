package info.seravee.wallmanager.business.dao;

import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.business.exception.NoDataFoundException;

public interface ConfigurationDao {
	Configuration get() throws NoDataFoundException;
	void store(Configuration data);
}
