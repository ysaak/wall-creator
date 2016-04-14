package info.seravee.wallcreator.ui.components;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;

import info.seravee.wallcreator.ui.GuiConstants;

public class GBCHelper {
	public static final double DEFAULT_WEIGHT = 0.0;
	public static final int DEFAULT_WIDTH = 1;
	public static final int DEFAULT_HEIGHT = 1;
	
	public static final int DEFAULT_ANCHOR = GridBagConstraints.CENTER;
	public static final int DEFAULT_FILL = GridBagConstraints.NONE;
	
	public static final Insets DEFAULT_INSETS = new Insets(GuiConstants.SMALL_SPACER, GuiConstants.SMALL_SPACER, GuiConstants.SMALL_SPACER, GuiConstants.SMALL_SPACER);
	
	private final JComponent component;
	
	private final Insets baseInsets;
	
	public GBCHelper(final JComponent component) {
		this(component, DEFAULT_INSETS);
	}
	
	public GBCHelper(final JComponent component, final Insets baseInsets) {
		if (component == null) {
			throw new IllegalArgumentException("Component cannot be null");
		}
		
		this.component = component;
		this.component.setLayout(new GridBagLayout());
		this.baseInsets = baseInsets;
	}
	
	
	public void addComponent(Component c, int gridX, int gridY) {
		addComponent(c, gridX, gridY, DEFAULT_WEIGHT, DEFAULT_WEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_ANCHOR, DEFAULT_FILL);
	}
	
	public void addComponent(Component c, int gridX, int gridY, final Insets insets) {
		addComponent(c, gridX, gridY, DEFAULT_WEIGHT, DEFAULT_WEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_ANCHOR, DEFAULT_FILL, insets);
	}
	
	public void addComponent(Component c, int gridX, int gridY, double weightX, double weightY, int width, int height, int anchor, int fill) {
		addComponent(c, gridX, gridY, weightX, weightY, width, height, anchor, fill, baseInsets);
	}
	
	public void addComponent(Component c, int gridX, int gridY, double weightX, double weightY, int width, int height, int anchor, int fill, Insets insets) {
		this.component.add(c, new GridBagConstraints(gridX, gridY, width, height, weightX, weightY, anchor,fill, insets, 0, 0));
	}
	
	public void addAnchoredComponent(Component c, int gridX, int gridY, int anchor) {
		addComponent(c, gridX, gridY, DEFAULT_WEIGHT, DEFAULT_WEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT, anchor, DEFAULT_FILL);
	}
	
	public void addFilledComponent(Component c, int gridX, int gridY, int fill) {
		addComponent(c, gridX, gridY, DEFAULT_WEIGHT, DEFAULT_WEIGHT, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_ANCHOR, fill);
	}
	
	public void addSpanningComponent(Component c, int gridX, int gridY, int width, int height) {
		addComponent(c, gridX, gridY, DEFAULT_WEIGHT, DEFAULT_WEIGHT, width, height, DEFAULT_ANCHOR, DEFAULT_FILL);
	}
	
	public void addSpanningFilledComponent(Component c, int gridX, int gridY, int width, int height, int fill) {
		addComponent(c, gridX, gridY, DEFAULT_WEIGHT, DEFAULT_WEIGHT, width, height, DEFAULT_ANCHOR, fill);
	}
}
