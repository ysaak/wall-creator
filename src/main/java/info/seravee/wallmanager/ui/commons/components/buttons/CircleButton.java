package info.seravee.wallmanager.ui.commons.components.buttons;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.Timer;

import info.seravee.wallmanager.ui.commons.laf.LafUtils;

public class CircleButton extends JButton {
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
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				startRippleEffect(e.getPoint());
			}
		});
	}

	protected Shape shape, base;

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

	private static final int RIPPLE_DELAY = 35;
	private static final int RIPPLE_DIAMETER_INC_VALUE = 5; 
	private static final Color RIPPLE_COLOR = new Color(1, 1, 1, 0.2f);
	
	private Area rippleArea;
	private Point rippleStartPoint = null;
	private int rippleDiameter = 0;
	private Timer rippleTimer = null;
	
	
	protected void startRippleEffect(Point mousePosition) {
		if (rippleTimer != null) {
			stopRippleEffect();
		}
		
		rippleStartPoint = new Point(mousePosition);
		rippleDiameter = 0;
		
		rippleTimer = new Timer(RIPPLE_DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rippleEffect();
			}
		});
		rippleTimer.setInitialDelay(0);
		rippleTimer.start();
	}

	protected void rippleEffect() {
		// https://github.com/traex/RippleEffect/blob/master/library/src/main/java/com/andexert/library/RippleView.java
		// https://github.com/balysv/material-ripple/blob/master/library/src/main/java/com/balysv/materialripple/MaterialRippleLayout.java
		
		initShape();
		
    	// Increase diameter
		rippleDiameter += RIPPLE_DIAMETER_INC_VALUE;
		
		// Adjust value for X/Y coordinate
		int coordAdjust = rippleDiameter / 2;
		
		final Shape rippleShape = new Ellipse2D.Float(rippleStartPoint.x - coordAdjust, rippleStartPoint.y - coordAdjust, rippleDiameter, rippleDiameter);
		final Area shapeArea = new Area(shape);
		
		rippleArea = new Area(rippleShape);
		rippleArea.intersect(shapeArea);
		
		
		repaint();
		
		shapeArea.subtract(rippleArea);
		
		if (shapeArea.isEmpty()) {
			stopRippleEffect();
		}
	}
	
	protected void stopRippleEffect() {
		// Stop timer
		if (rippleTimer != null) {
			rippleTimer.stop();
			rippleTimer = null;
		}
		
		// Reset vars
		rippleStartPoint = null;
		rippleDiameter = 0;
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
	    
	    if (rippleStartPoint != null) {
	    	
	    	g2.setColor(RIPPLE_COLOR);
	    	
	    	int coordAdjust = rippleDiameter / 2;
	    	
	    	Shape rippleShape = new Ellipse2D.Float(rippleStartPoint.x - coordAdjust, rippleStartPoint.y - coordAdjust, rippleDiameter, rippleDiameter);
	    	
	    	Area rippleArea = new Area(rippleShape);
	    	rippleArea.intersect(new Area(shape));
	    	
	    	g2.fill(rippleArea);
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
