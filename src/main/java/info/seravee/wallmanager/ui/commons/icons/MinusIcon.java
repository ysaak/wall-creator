package info.seravee.wallmanager.ui.commons.icons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

import info.seravee.wallmanager.ui.commons.laf.LafUtils;

public class MinusIcon implements Icon {
	private static final float SIZE_RATIO = 0.3f; 
	
	private final int size;
	private Color color;
	
	private boolean withRoundBackground;
	private Color roundBackgroundColor;
	private int roundBackgroundArc = 10;
	
	public MinusIcon(final int size) {
		this(size, Color.WHITE);
	}
	
	public MinusIcon(final int size, final Color color) {
		this.size = size;
		this.color = color;
		
		this.withRoundBackground = false;
		this.roundBackgroundColor = null;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		
		Graphics2D g2 = (Graphics2D) g;
		
		Color oldColor = g2.getColor();
		AffineTransform oldTransform = g2.getTransform();
		RenderingHints oldHints = g2.getRenderingHints();
		
		g2.translate(x, y);
		
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		
		
		// Draw background icon
		if (withRoundBackground) {
			g2.setColor(roundBackgroundColor);
			g2.fillRoundRect(x, y, getIconWidth(), getIconHeight(), roundBackgroundArc, roundBackgroundArc);
		}
		
		
		g2.setColor(color);
		
		
		final int barSize = Math.round(size * SIZE_RATIO);
		final int arcSize = Math.round(barSize * SIZE_RATIO);
		
		final int xyBase = Math.round((size / 2f) - (barSize / 2f));
		
		g2.fillRoundRect(0, xyBase, size, barSize, arcSize, arcSize);
		
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
