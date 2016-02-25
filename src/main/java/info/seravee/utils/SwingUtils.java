package info.seravee.utils;

import java.awt.Dimension;

import javax.swing.JComponent;

public class SwingUtils {
	public static final void setSMPSizes(JComponent comp, Dimension d) {
		comp.setSize(d);
		comp.setMinimumSize(d);
		comp.setPreferredSize(d);
	}
}
