package wallmanager.business.dao;

import java.util.List;

import wallmanager.beans.profile.Profile;
import wallmanager.business.exception.NoDataFoundException;
import wallmanager.business.exception.ProfileStoreException;

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
