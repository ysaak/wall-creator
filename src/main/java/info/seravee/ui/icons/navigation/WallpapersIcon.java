package info.seravee.ui.icons.navigation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

public class WallpapersIcon extends NavigationIcon {

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// Init graphics
		Graphics2D g2 = (Graphics2D) g;
		Color oldColor = g2.getColor();
		AffineTransform oldTransform = g2.getTransform();
		Stroke oldStroke = g2.getStroke();

		// Translate to paint location
		g2.translate(x, y);

		/*
		 * if (!coloredIcon) g2.setColor(Color.WHITE); else { // If colored, we
		 * add the screen color in blue g2.setColor(new Color(93, 156, 236));
		 * g2.fillRect(2, 2, 28, 18);
		 * 
		 * g2.setColor(Color.BLACK); }
		 */
		if (!coloredIcon)
			g2.setColor(Color.WHITE);

		// Draw border and background
		RoundRectangle2D.Double base = new RoundRectangle2D.Double(0, 5, 30, 20, 8, 8);
		RoundRectangle2D.Double innerBase = new RoundRectangle2D.Double(2, 7, 26, 16, 5, 5);

		if (coloredIcon) {
			g2.setColor(new Color(93, 156, 236));
			
			// Draw background only if colored
			g2.fill(base);
			
			g2.setColor(Color.BLACK);
		}
		
		
		Area border = new Area(base);
		border.subtract(new Area(innerBase));
		g2.fill(border);

		// Draw mountain
		if (coloredIcon)
			g2.setColor(new Color(101, 67, 33));
		
		g2.fill(getMountainShape());
		
		// Draw sun
		if (coloredIcon)
			g2.setColor(Color.YELLOW);
		
		g2.fillOval(4, 9, 5, 5);

		// Reset graphics data
		g2.setStroke(oldStroke);
		g2.setTransform(oldTransform);
		g2.setColor(oldColor);
	}

	private Shape getMountainShape() {

		int[] xPoints = {
			4, 
			11, 
			15,
			20,
			26
		};
		int[] yPoints = { 
			22, 
			14, 
			18,
			11,
			22
		};

		GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, xPoints.length);

		polyline.moveTo(xPoints[0], yPoints[0]);

		for (int index = 1; index < xPoints.length; index++) {
			polyline.lineTo(xPoints[index], yPoints[index]);
		}

		polyline.closePath();
		
		return polyline;
	}
}
