package info.seravee.wallmanager.ui.commons.icons.navigation;

import javax.swing.Icon;

public abstract class NavigationIcon implements Icon {

	protected boolean coloredIcon = false;

	@Override
	public int getIconWidth() {
		return 30;
	}

	@Override
	public int getIconHeight() {
		return 30;
	}
	
	public void setColoredIcon(boolean coloredIcon) {
		this.coloredIcon = coloredIcon;
	}
}
