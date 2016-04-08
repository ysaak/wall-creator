package info.seravee.wallmanager.ui.frame.events;

import info.seravee.wallmanager.beans.profile.Screen;
import lombok.Data;
import lombok.NonNull;

@Data
public class ScreenSelectedEvent {
	@NonNull
	private final Screen screen;
}
