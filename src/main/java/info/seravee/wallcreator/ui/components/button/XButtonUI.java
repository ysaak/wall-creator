package info.seravee.wallcreator.ui.components.button;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

import info.seravee.wallcreator.ui.components.LafUtils;

public class XButtonUI extends BasicButtonUI {
	
	private static final int ARC_SIZE = 10;
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
	    Dimension d = b.getSize();
	    
	    Graphics2D g2 = (Graphics2D) g;

	    g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
	    
	    // Draw background
	    drawBackground(g2, b, model, d);

	    // Draw icon if present
	    final boolean hasIcon = b.getIcon() != null;
	    final boolean hasText = b.getText() != null && b.getText().trim().length() > 0; 
	    
	    int iconWidth = 0;
	    if (hasIcon) {
	    	iconWidth = b.getIcon().getIconWidth() +  (b.getIconTextGap() * 2);

	    	
	    	int ix = (hasText) ? b.getIconTextGap() : (d.width - b.getIcon().getIconWidth()) / 2;
	    	int iy =  (d.height - b.getIcon().getIconHeight()) / 2;
	    	
	    	b.getIcon().paintIcon(b, g2, ix, iy);
	    }
	    
	    
	    if (hasText) {
	    	g.setFont(c.getFont());
	    	FontMetrics fm = g.getFontMetrics();
	    	
	    	g.setColor(b.getForeground());
	    	String caption = b.getText();
	    	int x = ((d.width - iconWidth - fm.stringWidth(caption)) / 2) + iconWidth;
	    	int y = (d.height + fm.getAscent()) / 2;
	    	g.drawString(caption, x, y);
	    }
	    
	}
	
	private void drawBackground(Graphics2D g2, AbstractButton button, ButtonModel model, Dimension dimension) {
		
		final XButtonColor color =  (XButtonColor) button.getClientProperty("x-button-color");
		
		if (model.isArmed()) {
			g2.setColor(color.getPressedColor());
		}
		else if (model.isSelected() || model.isRollover()) {
			g2.setColor(color.getHoverColor());
		}
		else {
			g2.setColor(color.getBaseColor());
		}
		
		final XButtonPosition position =  (XButtonPosition) button.getClientProperty("x-button-position");
		
		
		if (position == null || position == XButtonPosition.ALONE) {
			g2.fillRoundRect(0, 0, dimension.width, dimension.height, ARC_SIZE, ARC_SIZE);
		}
		else if (position == XButtonPosition.FIRST) {
			g2.fillRoundRect(0, 0, dimension.width, dimension.height, ARC_SIZE, ARC_SIZE);
			g2.fillRect(dimension.width - ARC_SIZE, 0, ARC_SIZE, dimension.height);
		}
		else if (position == XButtonPosition.LAST) {
			g2.fillRoundRect(0, 0, dimension.width, dimension.height, ARC_SIZE, ARC_SIZE);
			g2.fillRect(0, 0, ARC_SIZE, dimension.height);
		}
		else {
			g2.fillRect(0, 0, dimension.width, dimension.height);
		}
	}
}
