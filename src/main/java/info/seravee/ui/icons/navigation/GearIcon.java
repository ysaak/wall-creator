package info.seravee.ui.icons.navigation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class GearIcon extends NavigationIcon {

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		Color oldColor = g2.getColor();
		AffineTransform oldTransform = g2.getTransform();
		
		g2.translate(x, y);
		
		if (coloredIcon)
			g2.setColor(Color.DARK_GRAY);
		else 
			g2.setColor(Color.WHITE);
		
		// Donut part
		Ellipse2D ellipse = new Ellipse2D.Double(5, 5, 20, 20);
		Ellipse2D ellipseSubstract = new Ellipse2D.Double(11, 11, 8, 8);
		Area area = new Area(ellipse);
		area.subtract(new Area(ellipseSubstract));
		g2.fill(area);

		// Pics of the gear
		for (int i=0; i<8; i++) {
			g2.fillRoundRect(12, 1, 6, 6, 2, 2);
			g2.rotate(Math.PI / 4, 15, 15);
		}
		
		
		g2.setTransform(oldTransform);
		g2.setColor(oldColor);
	}

}
