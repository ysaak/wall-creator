package info.seravee.wallmanager.ui.frame.events;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import lombok.Data;
import lombok.NonNull;

@Data
public class ProfileVersionSelectedEvent {
	@NonNull
	private final Profile profile;
	
	@NonNull
	private final ProfileVersion version;
}
