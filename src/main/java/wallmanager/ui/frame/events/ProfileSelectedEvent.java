package wallmanager.ui.frame.events;

import lombok.Data;
import lombok.NonNull;
import wallmanager.beans.profile.Profile;

@Data
public class ProfileSelectedEvent {
	@NonNull
	private final Profile profile;
}
