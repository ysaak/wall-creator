package info.seravee.wallcreator.ui.components.button;


import javax.swing.Action;
import javax.swing.JButton;

public class XButton extends JButton {
	private static final long serialVersionUID = -3742978666559607378L;
	
	private static final XButtonColor DEFAULT_BUTTON_COLOR = XButtonColor.BLUE;
	private static final XButtonPosition DEFAULT_BUTTON_POSITION = XButtonPosition.ALONE;

	public XButton(Action action) {
		this(action, DEFAULT_BUTTON_COLOR, DEFAULT_BUTTON_POSITION);
	}

	public XButton(Action action, XButtonColor color) {
		this(action, color, DEFAULT_BUTTON_POSITION);
	}

	public XButton(Action action, XButtonPosition position) {
		this(action, DEFAULT_BUTTON_COLOR, position);
	}

	public XButton(Action action, XButtonColor color, XButtonPosition position) {
		super(action);
		
		setPosition(position);
		setColor(color);
		
		setUI(new XButtonUI());
		setBorderPainted(false);
		
		setIconTextGap(5);
	}

	public void setColor(XButtonColor color) {
		setBackground(null);
		setForeground(color.getTextColor());
		putClientProperty("x-button-color", color);
		repaint();
	}
	
	public XButtonColor getColor() {
		return (XButtonColor) getClientProperty("x-button-color");
	}
	
	
	public void setPosition(XButtonPosition position) {
		putClientProperty("x-button-position", position);
		repaint();
	}

	public XButtonPosition getPosition() {
		return (XButtonPosition) getClientProperty("x-button-position");
	}
}
