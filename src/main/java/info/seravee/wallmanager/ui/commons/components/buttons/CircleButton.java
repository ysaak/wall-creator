package info.seravee.wallmanager.ui.commons.components.buttons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;

import info.seravee.wallmanager.ui.commons.laf.LafUtils;

public class CircleButton extends RippleButton {
	private static final long serialVersionUID = 7240370990302640076L;

	public CircleButton(Action action) {
		super(action);

		setBorderPainted(false);
		setIconTextGap(0);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setAlignmentY(Component.TOP_ALIGNMENT);

		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		initShape();
	}
	
	protected Shape base;

	@Override
	protected void initShape() {
		if (!getBounds().equals(base)) {
			Dimension s = getPreferredSize();
			base = getBounds();
			shape = new Ellipse2D.Float(0, 0, s.width - 1, s.height - 1);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Icon icon = getIcon();
		Insets i = getInsets();
		int iw = Math.max(icon.getIconWidth(), icon.getIconHeight());
		return new Dimension(iw + i.right + i.left, iw + i.top + i.bottom);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		initShape();
		
		boolean bgPainted = false;
		Color bgColor = null;

		if (getModel().isArmed()) {
			bgPainted = true;
			bgColor = getBackground().darker();
		} 
		/*else if (getModel().isSelected() || getModel().isRollover()) {
			bgPainted = true;
			bgColor = getBackground();
		}*/
		
		Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);

	    if (bgPainted) {
	    	g2.setColor(bgColor);
	    	g2.fill(shape);
	    }
	    
	    
	    
		super.paintComponent(g);
	}

	@Override
	public boolean contains(int x, int y) {
		initShape();
		return shape.contains(x, y);
		// or return super.contains(x, y) && ((image.getRGB(x, y) >> 24) & 0xff) > 0;
	}

}
