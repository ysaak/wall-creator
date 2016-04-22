package info.seravee.wallmanager.business.dao;

import java.util.List;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.exception.NoDataFoundException;
import info.seravee.wallmanager.business.exception.profile.ProfileStoreException;

public interface ProfileDao {
	/**
	 * List stored profiles
	 * @return
	 * @throws ProfileStoreException 
	 */
	List<Profile> list();
	
	/**
	 * Store a profile
	 * @param profile
	 * @return
	 * @throws ProfileStoreException 
	 */
	Profile store(Profile profile);
	
	/**
	 * 
	 * @param profileID
	 * @return
	 * @throws NoDataFoundException
	 */
	Profile get(String profileID) throws NoDataFoundException;
}
