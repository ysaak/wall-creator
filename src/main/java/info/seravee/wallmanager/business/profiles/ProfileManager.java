package info.seravee.wallmanager.business.profiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.business.dao.ProfileDao;
import info.seravee.wallmanager.business.exception.NoDataFoundException;
import info.seravee.wallmanager.business.exception.profile.ConfigurationAlreadyUsedException;
import info.seravee.wallmanager.business.exception.profile.NameAlreadyUsedException;
import info.seravee.wallmanager.business.platform.PlatformService;

public class ProfileManager implements ProfileService {

	@Inject
	private ProfileDao profileDao;
	
	@Inject
	private PlatformService platformService;

	@Override
	public List<Profile> list() {
		List<Profile> profiles = profileDao.list();
		
		if (profiles == null) {
			profiles = new ArrayList<>();
		}

		if (profiles.size() == 0) {
			// No profile defined, create a default one
			final Profile defaultProfile;
			try {
				defaultProfile = createProfile("Default");
				profiles.add(defaultProfile);
			} catch (NameAlreadyUsedException | ConfigurationAlreadyUsedException e) {
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
	public Profile createProfile(String name/*, List<Screen> desktopConfiguration*/) throws NameAlreadyUsedException, ConfigurationAlreadyUsedException {
		
		List<Screen> configuration = platformService.getDesktopConfiguration();
		
		// Check for profile name unicity
		List<Profile> existingProfiles = profileDao.list();
		if (existingProfiles != null) {
			for (Profile p : existingProfiles) {
				if (p.getName().equalsIgnoreCase(name)) {
					throw new NameAlreadyUsedException("The name '" + name + "' is already used");
				}
				
				// Check configuration already exists
				if (checkConfigurationExists(p.getConfiguration(), configuration)) {
					throw new ConfigurationAlreadyUsedException("This configuration is already used");
				}
			}
		}
		
		Profile profile = new Profile();
		profile.setName(name);
		profile.setConfiguration(configuration);
		
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
	
	private boolean checkConfigurationExists(List<Screen> aList, List<Screen> bList) {
		// Two lists are null > OK
		if (aList == null && bList == null)
			return true;
		// One of the list is null > KO
		if (aList == null || bList == null)
			return false;
		
		// Lists have the same size
		if (aList.size() == bList.size()) {
			final List<Screen> workList = new ArrayList<>(bList);
			
			for (Screen screen : aList) {
				
				boolean found = false;
				
				for (int i=0; i<workList.size(); i++) {
					if (screen.equals(workList.get(i))) {
						workList.remove(i);
						found = true;
						break;
					}
				}
				
				if (!found) {
					// Element not found
					return false;
				}
			}

			// All elements found
			return true;
		}
		
		return false;
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
