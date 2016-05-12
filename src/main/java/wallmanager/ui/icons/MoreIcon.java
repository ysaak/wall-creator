package wallmanager.ui.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

import wallmanager.ui.commons.laf.LafUtils;

public class MoreIcon implements Icon {
	private static final float SIZE_RATIO = 0.3f; 
	
	private final int size;
	private Color color;
	
	public MoreIcon(final int size) {
		this(size, Color.WHITE);
	}
	
	public MoreIcon(final int size, final Color color) {
		this.size = size;
		this.color = color;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		Color oldColor = g2.getColor();
		AffineTransform oldTransform = g2.getTransform();
		RenderingHints oldHints = g2.getRenderingHints();
		
		g2.translate(x, y);
		
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		
		
		g2.setColor(color);
		
		int dotWidth = Math.round(size / 5f);
		final int arcSize = Math.round(dotWidth * SIZE_RATIO);
		
		int xBase = Math.round(size / 2f) - Math.round(dotWidth / 2f);
		
		
		g2.fillRoundRect(xBase, 0, dotWidth, dotWidth, arcSize, arcSize);
		g2.fillRoundRect(xBase, dotWidth * 2, dotWidth, dotWidth, arcSize, arcSize);
		g2.fillRoundRect(xBase, dotWidth * 4, dotWidth, dotWidth, arcSize, arcSize);
		
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
	
	public void setColor(Color color) {
		this.color = color;
	}
}
