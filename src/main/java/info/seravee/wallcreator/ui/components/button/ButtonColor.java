package info.seravee.wallcreator.ui.components.button;

import java.awt.Color;

public class ButtonColor {
	private final Color baseColor;
	private final Color hoverColor;
	private final Color pressedColor;
	
	private final Color textColor;

	public ButtonColor(Color baseColor) {
		this(baseColor, baseColor.darker(), baseColor.darker().darker(), Color.BLACK);
	}
	
	public ButtonColor(Color baseColor, Color textColor) {
		this(baseColor, baseColor.darker(), baseColor.darker().darker(), textColor);
	}
	
	public ButtonColor(Color baseColor, Color hoverColor, Color pressedColor, Color textColor) {
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
	
	public static final ButtonColor YELLOW = new ButtonColor(new Color(181, 137, 0));
	public static final ButtonColor ORANGE = new ButtonColor(new Color(203, 75, 22));
	public static final ButtonColor RED = new ButtonColor(new Color(220, 50, 47));
	public static final ButtonColor MAGENTA = new ButtonColor(new Color(211, 54, 130));
	public static final ButtonColor VIOLET = new ButtonColor(new Color(108, 196, 196));
	public static final ButtonColor BLUE = new ButtonColor(new Color(38, 139, 210), Color.WHITE);
	public static final ButtonColor CYAN = new ButtonColor(new Color(42, 161, 152));
	public static final ButtonColor GREEN = new ButtonColor(new Color(133, 153, 0));
}
