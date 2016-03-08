package info.seravee.wallcreator.ui.components.button;

import javax.swing.JButton;

public class XButton extends JButton {
	
	public XButton() {
		setUI(XButtonUI.createUI(ButtonColor.BLUE));
		setBorderPainted(false);
		
		setForeground(ButtonColor.BLUE.getTextColor());
		setIconTextGap(5);
	}
	
	public void setPosition(XButtonPosition position) {
		putClientProperty("x-button-position", position);
	}
	
	public XButtonPosition getPosition() {
		return (XButtonPosition) getClientProperty("x-button-position");
	}
}
