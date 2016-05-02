package info.seravee.wallmanager.business.profiles;

import java.io.IOException;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.business.dao.ProfileDao;
import info.seravee.wallmanager.business.exception.NoDataFoundException;
import info.seravee.wallmanager.business.exception.profile.NameAlreadyUsedException;

public class ProfileManager implements ProfileService {

	@Inject
	private ProfileDao profileDao;

	@Override
	public List<Profile> list() {
		List<Profile> profiles = profileDao.list();

		if (profiles.size() == 0) {
			// No profile defined, create a default one
			final Profile defaultProfile;
			try {
				defaultProfile = createProfile("Default");
				profiles.add(defaultProfile);
			} catch (NameAlreadyUsedException e) {
				// Should not append since no profile exists
			}
		}
		
		return profiles;
	}
	
	public Profile get(String profileID) throws NoDataFoundException {
		return profileDao.get(profileID);
	}

	@Override
	public Profile store(Profile profile) {
		return profileDao.store(profile);
	}
	
	/* --- Profile's actions --- */
	
	@Override
	public Profile createProfile(String name/*, List<Screen> desktopConfiguration*/) throws NameAlreadyUsedException {
		
		// Check for profile name unicity
		List<Profile> existingProfiles = profileDao.list();
		for (Profile p : existingProfiles) {
			if (p.getName().equalsIgnoreCase(name)) {
				throw new NameAlreadyUsedException("The name '" + name + "' is already used");
			}
		}
		
		Profile profile = new Profile();
		profile.setName(name);
		profile.setConfiguration(Platforms.get().getDesktopConfiguration());
		
		// Create default version
		ProfileVersion version = new ProfileVersion();
		version.setName("Default");
		version.setPreferred(true);
		
		for (Screen screen : profile.getConfiguration()) {
			WallpaperParameters parameters = new WallpaperParameters();
			parameters.setScreenId(screen.getId());
			
			version.getParameters().add(parameters);
		}
		
		profile.getVersions().add(version);
		
		return store(profile);
	}
	
	/* --- Versions --- */

	@Override
	public Profile deleteVersion(Profile profile, ProfileVersion versionToDelete) throws IOException {
		Preconditions.checkNotNull(profile);
		Preconditions.checkNotNull(versionToDelete);
		
		profile.getVersions().remove(versionToDelete);
		
		if (versionToDelete.isPreferred()) {
			profile.getVersions().get(0).setPreferred(true);
		}
		
		
		return profile;
	}
}
