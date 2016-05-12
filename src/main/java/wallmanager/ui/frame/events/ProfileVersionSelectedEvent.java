package wallmanager.ui.frame.events;

import lombok.Data;
import lombok.NonNull;
import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.ProfileVersion;

@Data
public class ProfileVersionSelectedEvent {
	@NonNull
	private final Profile profile;
	
	@NonNull
	private final ProfileVersion version;
}
