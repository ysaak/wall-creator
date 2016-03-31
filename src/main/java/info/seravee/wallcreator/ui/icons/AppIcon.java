package info.seravee.wallcreator.ui.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;

import info.seravee.wallcreator.ui.components.LafUtils;

public class AppIcon implements Icon {
	
	public static final AppIcon get16() {
		return new AppIcon(1);
	}
	
	public static final AppIcon get32() {
		return new AppIcon(2);
	}
	
	public static final AppIcon get48() {
		return new AppIcon(3);
	}
	
	public static final AppIcon get64() {
		return new AppIcon(4);
	}
	
	public static final AppIcon get128() {
		return new AppIcon(8);
	}
	
	public static final AppIcon getTrayIcon() {
		return new AppIcon(1, true);
	}
	
	private final int factor;
	private final boolean withBorder;
	
	private AppIcon(int factor) {
		this(factor, false);
	}
	
	private AppIcon(int factor, boolean withBorder) {
		this.factor = factor;
		this.withBorder = withBorder;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		Color oldColor = g.getColor();
		
		g.translate(x, y);
		
		g.setColor(Color.WHITE);
		g2.fill(getShape());
		
		if (withBorder) {
			g.setColor(Color.BLACK);
			g2.draw(getShape());
		}
		
		g.setColor(oldColor);
		g.translate(-x, -y);
	}
	
	
	private Shape getShape() {

		int[] xPoints = {
			0, 
			4,
			3, 
			7,
			8,
			12,
			11,
			16,
			9,
			8,
			7,
			6
		};
		int[] yPoints = { 
			0,
			11,
			16, 
			12,
			12,
			16,
			11,
			0,
			9,
			7,
			7,
			9
		};

		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);

		polyline.moveTo(xPoints[0] * factor, yPoints[0] * factor);

		for (int index = 1; index < xPoints.length; index++) {
			polyline.lineTo(xPoints[index] * factor, yPoints[index] * factor);
		}

		polyline.closePath();
		
		return polyline;
	}
	

	@Override
	public int getIconWidth() {
		return 16 * factor;
	}

	@Override
	public int getIconHeight() {
		return 16 * factor;
	}

}
