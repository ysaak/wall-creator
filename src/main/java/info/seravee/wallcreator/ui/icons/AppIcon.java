package info.seravee.wallcreator.ui.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import javax.swing.Icon;

public class AppIcon implements Icon {
	
	private static final int FACTOR = 1;
	private boolean withBorder = true;

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;
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

		polyline.moveTo(xPoints[0] * FACTOR, yPoints[0] * FACTOR);

		for (int index = 1; index < xPoints.length; index++) {
			polyline.lineTo(xPoints[index] * FACTOR, yPoints[index] * FACTOR);
		}

		polyline.closePath();
		
		return polyline;
	}
	

	@Override
	public int getIconWidth() {
		return 16 * FACTOR;
	}

	@Override
	public int getIconHeight() {
		return 16 * FACTOR;
	}

}
