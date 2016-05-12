package wallmanager.ui.frame.wallpapers;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import wallmanager.ui.commons.SolarizedColor;

public class FolderList<T> extends JList<T> {
	private static final long serialVersionUID = 5962590515579500161L;

	protected int hoveredIndex = -1;

	public FolderList(ListModel<T> dataModel) {
		super(dataModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new FolderListCellRendered());
		this.setBorder(null);
		this.setFixedCellHeight(24);

		this.setSelectionBackground(SolarizedColor.BLUE);
		this.setSelectionForeground(Color.WHITE);
	}
	
	private boolean processEvent(MouseEvent e) {
        int index = locationToIndex(e.getPoint());
        return index > -1 && getCellBounds(index, index).contains(e.getPoint());
    }
	
	@Override
	protected void processMouseEvent(MouseEvent e) {
		if (processEvent(e)) {
			super.processMouseEvent(e);
		}
		
		if (e.getID() == MouseEvent.MOUSE_EXITED) {
			hoveredIndex = -1;
			repaint();
		}
	}
	
	@Override
	protected void processMouseMotionEvent(MouseEvent e) {
		if (processEvent(e)) {
			super.processMouseMotionEvent(e);
			
			hoveredIndex = locationToIndex(e.getPoint());
		}
		else {
			hoveredIndex = -1;
		}
		repaint();
	}

	private class FolderListCellRendered extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		private final Border baseBorder = new EmptyBorder(0, 5, 0, 5);
		private final Border lineBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, SolarizedColor.BASE2);

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			File folder = (File) value;

			JLabel component = (JLabel) super.getListCellRendererComponent(list, folder.getName(), index, isSelected,
					cellHasFocus);

			component.setOpaque(true);
			component.setToolTipText(folder.getAbsolutePath());
			
			if (index == hoveredIndex && !isSelected && !cellHasFocus) {
				component.setBackground(SolarizedColor.BASE2);
			}

			if (index > 0) {
				component.setBorder(BorderFactory.createCompoundBorder(lineBorder, baseBorder));
			} else {
				component.setBorder(baseBorder);
			}

			return component;
		}
	}
}
