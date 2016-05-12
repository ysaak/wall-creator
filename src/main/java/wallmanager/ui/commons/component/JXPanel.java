package wallmanager.ui.commons.component;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import wallmanager.ui.commons.laf.LafUtils;

public class JXPanel extends JPanel {
	private static final long serialVersionUID = 1927477880658229817L;
	
	private boolean topLeftRounded = false;
	private boolean topRightRounded = false;
	private boolean bottomLeftRounded = false;
	private boolean bottomRightRounded = false;
	
	private int arcSize = 10;
	
	public JXPanel() {
		this(new BorderLayout(10, 10));
	}
	
	public JXPanel(LayoutManager layout) {
		super(layout);
		setOpaque(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		

		g2d.setColor(getBackground());
		
		g2d.fillRect(0, arcSize, getWidth(), getHeight() - 2*arcSize);
		g2d.fillRect(arcSize, 0, getWidth() - 2*arcSize, getHeight());
		
		
		if (topLeftRounded) {
			g2d.fillArc(0, 0, 2*arcSize, 2*arcSize, 90, 90);
		}
		else {
			g2d.fillRect(0, 0, arcSize, arcSize);
		}
		
		if (topRightRounded) {
			g2d.fillArc(getWidth() - 2*arcSize, 0, 2*arcSize, 2*arcSize, 0, 90);
		}
		else {
			g2d.fillRect(getWidth() - arcSize, 0, arcSize, arcSize);
		}
		
		if (bottomLeftRounded) {
			g2d.fillArc(0, getHeight()-2*arcSize, 2*arcSize, 2*arcSize, 180, 90);
		}
		else {
			g2d.fillRect(0, getHeight() - arcSize, arcSize, arcSize);
		}
		
		if (bottomRightRounded) {
			g2d.fillArc(getWidth() - 2*arcSize, getHeight()-2*arcSize, 2*arcSize, 2*arcSize, -90, 90);
		}
		else {
			g2d.fillRect(getWidth() - arcSize, getHeight() - arcSize, arcSize, arcSize);
		}
	}
	
	public JXPanel allRounded() {
		topRounded();
		bottomRounded();
		return this;
	}
	
	public JXPanel topRounded() {
		this.topLeftRounded = true;
		this.topRightRounded = true;
		return this;
	}
	
	public JXPanel bottomRounded() {
		this.bottomLeftRounded = true;
		this.bottomRightRounded = true;
		return this;
	}
	
	public JXPanel leftRounded() {
		this.topLeftRounded = true;
		this.bottomLeftRounded = true;
		return this;
	}
	
	public JXPanel rightRounded() {
		this.topRightRounded = true;
		this.bottomRightRounded = true;
		return this;
	}

	public void setTopLeftRounded(boolean topLeftRounded) {
		this.topLeftRounded = topLeftRounded;
	}

	public void setTopRightRounded(boolean topRightRounded) {
		this.topRightRounded = topRightRounded;
	}

	public void setBottomLeftRounded(boolean bottomLeftRounded) {
		this.bottomLeftRounded = bottomLeftRounded;
	}

	public void setBottomRightRounded(boolean bottomRightRounded) {
		this.bottomRightRounded = bottomRightRounded;
	}
	
	
}
