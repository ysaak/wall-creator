package info.seravee.wallmanager.ui.commons.icons.navigation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class MonitorIcon extends NavigationIcon {
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		// Init graphics
		Graphics2D g2 = (Graphics2D) g;
		Color oldColor = g2.getColor();
		AffineTransform oldTransform = g2.getTransform();
		Stroke oldStroke = g2.getStroke();
		
		// Translate to paint location
		g2.translate(x, y);
		

		if (!coloredIcon)
			g2.setColor(Color.WHITE);
		else {
			// If colored, we add the screen color in blue
			g2.setColor(new Color(93, 156, 236));
			g2.fillRect(2, 2, 28, 18);
			
			g2.setColor(Color.BLACK);
		}

		// Draw icon
		g2.setStroke(new BasicStroke(3f));
		g2.drawRoundRect(0, 0, 30, 20, 10, 10);
		
		g2.setStroke(new BasicStroke(1f));
		
		g2.fillRect(2, 15, 27, 4);
		
		g2.fillRect(12, 20, 8, 10);

		
		g2.fillRect(10, 26, 12, 4);
		g2.fillArc(6, 26, 8, 8, 90, 90);
		g2.fillArc(18, 26, 8, 8, 0, 90);
		
		if (!coloredIcon)
			g2.setColor(Color.BLACK);
		else
			g2.setColor(Color.WHITE);
		
		g2.fillOval(14, 17, 3, 3);
		
		// Reset graphics data
		g2.setStroke(oldStroke);
		g2.setTransform(oldTransform);
		g2.setColor(oldColor);
	}
}
