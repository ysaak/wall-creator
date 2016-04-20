package info.seravee.wallmanager.ui.frame.desktop;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;

interface ProfileEditorListener {
	void resetProfileVersion(Profile profile, ProfileVersion version);
	void storeProfile(Profile profile, ProfileVersion version, boolean setAsWallpaper);
}
