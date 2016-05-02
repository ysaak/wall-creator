package info.seravee.wallmanager.business.profiles;

import java.io.IOException;
import java.util.List;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.business.exception.NoDataFoundException;
import info.seravee.wallmanager.business.exception.profile.NameAlreadyUsedException;
import info.seravee.wallmanager.business.exception.profile.ProfileStoreException;

public interface ProfileService {
	/**
	 * List stored profiles
	 * @return
	 * @throws ProfileStoreException 
	 */
	List<Profile> list() throws ProfileStoreException;
	
	/**
	 * Store a profile
	 * @param profile
	 * @return 
	 * @throws ProfileStoreException 
	 */
	Profile store(Profile profile) throws ProfileStoreException;
	
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
	 * @throws ProfileStoreException
	 * @throws NameAlreadyUsedException 
	 */
	Profile createProfile(String name) throws ProfileStoreException, NameAlreadyUsedException;
	
	
	/* --- Versions --- */
	
	Profile deleteVersion(Profile profile, ProfileVersion versionToDelete) throws IOException;
}
