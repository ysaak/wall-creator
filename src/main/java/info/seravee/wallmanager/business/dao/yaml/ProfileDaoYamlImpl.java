package info.seravee.wallmanager.business.dao.yaml;

import java.nio.file.Path;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.dao.ProfileDao;

public class ProfileDaoYamlImpl extends AbstractYamlStore<Profile> implements ProfileDao {

	public ProfileDaoYamlImpl() {
		super(new ProfileContructor(), new ProfileRepresenter());
	}
	
	@Override
	public Profile store(Profile profile) {
		super.store(profile, profile.getId());
		return profile;
	}

	@Override
	protected Path getStoreLocation() {
		return Platforms.get().getAppDirectory().resolve("profiles");
	}
}
