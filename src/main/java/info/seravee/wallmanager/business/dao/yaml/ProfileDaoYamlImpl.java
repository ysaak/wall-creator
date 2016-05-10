package info.seravee.wallmanager.business.dao.yaml;

import java.nio.file.Path;

import com.google.inject.Inject;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.dao.ProfileDao;
import info.seravee.wallmanager.business.platform.PlatformService;

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
