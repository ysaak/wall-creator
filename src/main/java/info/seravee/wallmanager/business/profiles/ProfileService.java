package info.seravee.wallmanager.business.profiles;

import java.io.IOException;
import java.util.List;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.business.exception.NoDataFoundException;
import info.seravee.wallmanager.business.exception.profile.ConfigurationAlreadyUsedException;
import info.seravee.wallmanager.business.exception.profile.NameAlreadyUsedException;

public interface ProfileService {
	/**
	 * List stored profiles
	 * @return
	 */
	List<Profile> list();
	
	/**
	 * Store a profile
	 * @param profile
	 * @return 
	 */
	Profile store(Profile profile);
	
	/**
	 * 
	 * @param profileID
	 * @return
	 * @throws NoDataFoundException
	 */
	Profile get(String profileID) throws NoDataFoundException;
	
	
	/* --- Profiles --- */
	/**
	 * Create a new profile with the specified name based on the current configuration
	 * @param name New profile name
	 * @return new profile
	 * @throws NameAlreadyUsedException 
	 * @throws ConfigurationAlreadyUsedException 
	 */
	Profile createProfile(String name) throws NameAlreadyUsedException, ConfigurationAlreadyUsedException;
	
	
	/* --- Versions --- */
	
	Profile deleteVersion(Profile profile, ProfileVersion versionToDelete) throws IOException;
}
