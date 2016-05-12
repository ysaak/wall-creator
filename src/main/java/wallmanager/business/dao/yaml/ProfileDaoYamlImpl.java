package wallmanager.business.dao.yaml;

import java.nio.file.Path;

import com.google.inject.Inject;

import wallmanager.beans.profile.Profile;
import wallmanager.business.dao.ProfileDao;
import wallmanager.business.platform.PlatformService;

public class ProfileDaoYamlImpl extends AbstractYamlStore<Profile> implements ProfileDao {
	
	private final PlatformService platformService; 

	@Inject
	public ProfileDaoYamlImpl(PlatformService platformService) {
		super(new ProfileContructor(), new ProfileRepresenter());
		this.platformService = platformService;
	}
	
	@Override
	public Profile store(Profile profile) {
		super.store(profile, profile.getId());
		return profile;
	}

	@Override
	protected Path getStoreLocation() {
		return platformService.getAppDirectory().resolve("profiles");
	}
}
