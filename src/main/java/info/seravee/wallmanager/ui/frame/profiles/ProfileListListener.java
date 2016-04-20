package info.seravee.wallmanager.ui.frame.profiles;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;

interface ProfileListListener {
	void profileCreate(String name);
	void profileUpdated(Profile profile);
	
	void markProfileVersionAsPrefered(Profile profile, ProfileVersion version);
	
	void profileSelected(Profile profile);
	void profileVersionSelected(Profile profile, ProfileVersion version);
}
