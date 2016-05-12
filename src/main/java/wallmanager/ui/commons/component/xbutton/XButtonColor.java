package wallmanager.ui.commons.component.xbutton;

import java.awt.Color;

public class XButtonColor {
	private final Color baseColor;
	private final Color hoverColor;
	private final Color pressedColor;
	
	private final Color textColor;

	public XButtonColor(Color baseColor) {
		this(baseColor, baseColor.darker(), baseColor.darker().darker(), Color.BLACK);
	}
	
	public XButtonColor(Color baseColor, Color textColor) {
		this(baseColor, baseColor.darker(), baseColor.darker().darker(), textColor);
	}
	
	public XButtonColor(Color baseColor, Color hoverColor, Color pressedColor, Color textColor) {
		this.baseColor = baseColor;
		this.hoverColor = hoverColor;
		this.pressedColor = pressedColor;
		this.textColor = textColor;
	}

	public Color getBaseColor() {
		return baseColor;
	}
	
	public Color getHoverColor() {
		return hoverColor;
	}

	public Color getPressedColor() {
		return pressedColor;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public static final XButtonColor YELLOW = new XButtonColor(new Color(181, 137, 0));
	public static final XButtonColor ORANGE = new XButtonColor(new Color(203, 75, 22));
	public static final XButtonColor RED = new XButtonColor(new Color(220, 50, 47));
	public static final XButtonColor MAGENTA = new XButtonColor(new Color(211, 54, 130));
	public static final XButtonColor VIOLET = new XButtonColor(new Color(108, 196, 196));
	public static final XButtonColor BLUE = new XButtonColor(new Color(38, 139, 210), Color.WHITE);
	public static final XButtonColor CYAN = new XButtonColor(new Color(42, 161, 152));
	public static final XButtonColor GREEN = new XButtonColor(new Color(133, 153, 0));
}
