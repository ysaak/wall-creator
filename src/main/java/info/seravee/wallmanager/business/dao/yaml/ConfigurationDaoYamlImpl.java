package info.seravee.wallmanager.business.dao.yaml;

import java.nio.file.Path;

import org.yaml.snakeyaml.representer.Representer;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.business.dao.ConfigurationDao;
import info.seravee.wallmanager.business.exception.NoDataFoundException;

public class ConfigurationDaoYamlImpl extends AbstractYamlStore<Configuration> implements ConfigurationDao {
	private static final String CONFIG_FILE_NAME = "config";
	
	public ConfigurationDaoYamlImpl() {
		super(new ConfigurationConstructor(), new Representer());
	}

	@Override
	public Configuration get() throws NoDataFoundException {
		return get(CONFIG_FILE_NAME);
	}

	@Override
	public void store(Configuration data) {
		store(data, CONFIG_FILE_NAME);
	}

	@Override
	protected Path getStoreLocation() {
		return Platforms.get().getAppDirectory();
	}
}
