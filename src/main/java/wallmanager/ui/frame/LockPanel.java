package wallmanager.ui.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import wallmanager.ui.commons.GBCHelper;
import wallmanager.ui.commons.laf.LafUtils;

class LockPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 4154724480316654793L;

	private final JLabel infoLabel;
	
	private final DotIcon dot1Icon = new DotIcon();
	private final DotIcon dot2Icon = new DotIcon();
	private final DotIcon dot3Icon = new DotIcon();
	
	private final JLabel dot1Label = new JLabel(dot1Icon);
	private final JLabel dot2Label = new JLabel(dot2Icon);
	private final JLabel dot3Label = new JLabel(dot3Icon);
	
	int step = 0;
	
	private Timer timer = null;

	public LockPanel() {
		setDoubleBuffered(true);
		setOpaque(false);
		
		infoLabel = new JLabel(" ", SwingConstants.CENTER);
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD, 26f));
		
		GBCHelper gblh = new GBCHelper(this, new Insets(20, 20, 20, 20));
		gblh.addComponent(infoLabel, 0, 0, 2.0, 2.0, 5, 1, GridBagConstraints.PAGE_END, GridBagConstraints.HORIZONTAL);
		
		gblh.addComponent(Box.createHorizontalGlue(), 0, 1, 10., 2., 1, 1, GBCHelper.DEFAULT_ANCHOR, GBCHelper.DEFAULT_FILL);
		gblh.addComponent(dot1Label, 1, 1, 1., 1., 1, 1, GridBagConstraints.PAGE_START, GBCHelper.DEFAULT_FILL);
		gblh.addComponent(dot2Label, 2, 1, 1., 1., 1, 1, GridBagConstraints.PAGE_START, GBCHelper.DEFAULT_FILL);
		gblh.addComponent(dot3Label, 3, 1, 1., 1., 1, 1, GridBagConstraints.PAGE_START, GBCHelper.DEFAULT_FILL);
		gblh.addComponent(Box.createHorizontalGlue(), 4, 1, 10., 2., 1, 1, GBCHelper.DEFAULT_ANCHOR, GBCHelper.DEFAULT_FILL);
	}
	
	public void setMessage(String message) {
		infoLabel.setText(message);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if (visible && timer == null) {
			timer = new Timer(400, this);
			timer.setInitialDelay(0);
			timer.start();
		}
		else if (!visible && timer != null) {
			timer.stop();
			timer = null;
		}
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(0,0,0, 0.6f));
		g2d.fillRect(0, 0, getWidth(), getHeight());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (step == 0 || step == 3) {
			dot1Icon.setVisible(step == 0);
			//startFadeTimer(dot1Icon, step == 0 ? 1 : -1);
		}
		else if (step == 1 || step == 4) {
			dot2Icon.setVisible(step == 1);
			//startFadeTimer(dot2Icon, step == 1 ? 1 : -1);
		}
		else if (step == 2 || step == 5) {
			dot3Icon.setVisible(step == 2);
			//startFadeTimer(dot3Icon, step == 2 ? 1 : -1);
		}
		
		step++;
		
		if (step > 5) {
			step = 0;
		}
		
		repaint();
	}
	
	private class DotIcon implements Icon {
		private float opacity = 0;
		
		public void setVisible(boolean visible) {
			opacity = visible ? 0 : 1;
		}
		
		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			
			Graphics2D g2 = (Graphics2D) g;
			
			Color oldColor = g2.getColor();
			AffineTransform oldTransform = g2.getTransform();
			RenderingHints oldHints = g2.getRenderingHints();
			
			g2.translate(x, y);
			
			g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
			
			g2.setColor(new Color(1, 1, 1, opacity));
			g2.fillOval(0, 0, getIconWidth(), getIconHeight());

			
			g2.setTransform(oldTransform);
			g2.setColor(oldColor);
			g2.setRenderingHints(oldHints);
		}
		
		@Override
		public int getIconWidth() {
			return 60;
		}

		@Override
		public int getIconHeight() {
			return 60;
		}
	}
	
}
