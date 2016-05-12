package wallmanager.ui.frame.events;

import lombok.Data;
import lombok.NonNull;
import wallmanager.beans.profile.Screen;

@Data
public class ScreenSelectedEvent {
	@NonNull
	private final Screen screen;
}
