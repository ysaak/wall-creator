package info.seravee.wallcreator.ui.components;

import java.awt.RenderingHints;

public class LafUtils {
	public static final RenderingHints ANTIALIASING_HINTS;
	static {
		ANTIALIASING_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ANTIALIASING_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
}
