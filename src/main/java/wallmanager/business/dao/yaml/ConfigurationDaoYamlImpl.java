package wallmanager.business.dao.yaml;

import java.nio.file.Path;

import org.yaml.snakeyaml.representer.Representer;

import com.google.inject.Inject;

import wallmanager.beans.Configuration;
import wallmanager.business.dao.ConfigurationDao;
import wallmanager.business.exception.NoDataFoundException;
import wallmanager.business.platform.PlatformService;

public class ConfigurationDaoYamlImpl extends AbstractYamlStore<Configuration> implements ConfigurationDao {
	private static final String CONFIG_FILE_NAME = "config";
	
	private final PlatformService platformService; 
	
	@Inject
	public ConfigurationDaoYamlImpl(PlatformService platformService) {
		super(new ConfigurationConstructor(), new Representer());
		this.platformService = platformService;
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
		return platformService.getAppDirectory();
	}
}
