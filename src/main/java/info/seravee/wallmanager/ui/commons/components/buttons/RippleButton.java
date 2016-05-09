package info.seravee.wallmanager.ui.commons.components.buttons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.Timer;

import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.ui.commons.laf.LafUtils;

abstract class RippleButton extends JButton {
	private static final long serialVersionUID = -3870993709856338061L;
	
	private static final int RIPPLE_DELAY = 30;
	private static final int RIPPLE_DIAMETER_INC_VALUE = 10;
	private static final float RIPPLE_INITIAL_OPACITY = 0.5f;
	private static final float RIPPLE_OPACITY_INC_VALUE = 0.05f;
	
	
	protected Shape shape;
	
	private Color rippleColor = new Color(1, 1, 1, RIPPLE_INITIAL_OPACITY);
	
	
	private Area rippleArea;
	private Point rippleStartPoint = null;
	private int rippleDiameter = 0;
	private float rippleOpacity;
	private Timer rippleTimer = null;
	
	private boolean rippleReduceOpacity = false;
	private boolean rippleGrowInk = true;

	public RippleButton(Action action) {
		super(action);
		
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				startRippleEffect(e.getPoint());
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				rippleReduceOpacity = true;
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		if (rippleStartPoint != null) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
	    	
	    	g2.setColor(rippleColor);
	    	
	    	int coordAdjust = rippleDiameter / 2;
	    	
	    	Shape rippleShape = new Ellipse2D.Float(rippleStartPoint.x - coordAdjust, rippleStartPoint.y - coordAdjust, rippleDiameter, rippleDiameter);
	    	
	    	Area rippleArea = new Area(rippleShape);
	    	rippleArea.intersect(new Area(shape));
	    	
	    	g2.fill(rippleArea);
	    }
		
		super.paintComponent(g);
	}
	
	protected abstract void initShape();
	
	protected void startRippleEffect(Point mousePosition) {
		if (!isEnabled()) {
			return;
		}
		
		if (rippleTimer != null) {
			stopRippleEffect();
		}
		
		rippleStartPoint = new Point(mousePosition);
		rippleDiameter = 0;
		rippleGrowInk = true;
		rippleReduceOpacity = false;
		rippleOpacity = RIPPLE_INITIAL_OPACITY;
		rippleColor = new Color(
			rippleColor.getRed() / 255f,
			rippleColor.getGreen() / 255f,
			rippleColor.getBlue() / 255f,
			rippleOpacity
		);
		
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
		initShape();
		final Area shapeArea = new Area(shape);
		
		if (rippleGrowInk) {
		
	    	// Increase diameter
			rippleDiameter += RIPPLE_DIAMETER_INC_VALUE;
			
			// Adjust value for X/Y coordinate
			int coordAdjust = rippleDiameter / 2;
			
			final Shape rippleShape = new Ellipse2D.Float(rippleStartPoint.x - coordAdjust, rippleStartPoint.y - coordAdjust, rippleDiameter, rippleDiameter);
			
			rippleArea = new Area(rippleShape);
			rippleArea.intersect(shapeArea);
		}
		
		if (rippleReduceOpacity) {
			rippleOpacity -= RIPPLE_OPACITY_INC_VALUE;
			
			rippleColor = new Color(
				rippleColor.getRed() / 255f,
				rippleColor.getGreen() / 255f,
				rippleColor.getBlue() / 255f,
				rippleOpacity
			);
		}
		
		if (rippleGrowInk || rippleReduceOpacity) {
			// Repaint only if changes are made
			repaint();
		}
		
		if (shapeArea.equals(rippleArea)) {
			rippleGrowInk = false;
		}
		
		if (rippleOpacity <= 0f) {
			// Ink no longer visible 
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
	public void setBackground(Color bg) {
		rippleColor = GraphicsUtilities.getForegroundFromBackground(bg);
		
		super.setBackground(bg);
	}
}
