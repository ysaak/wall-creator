package wallmanager.ui.commons.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

import wallmanager.ui.commons.laf.LafUtils;

class FrameMinimizeIcon implements Icon {
	private final int size = 14;
	
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		final Graphics2D g2 = (Graphics2D) g;
		
		final Color oldColor = g2.getColor();
		final AffineTransform oldTransform = g2.getTransform();
		final RenderingHints oldHints = g2.getRenderingHints();
		
		g2.translate(x, y);
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		
		g2.setColor(Color.WHITE);
		
		g2.fillRect(2, getIconHeight()-4, getIconWidth()-4, 2);

		g2.setTransform(oldTransform);
		g2.setColor(oldColor);
		g2.setRenderingHints(oldHints);
	}

	@Override
	public int getIconWidth() {
		return size;
	}

	@Override
	public int getIconHeight() {
		return size;
	}
}
