package info.seravee.wallcreator.ui.lister;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import info.seravee.wallcreator.ui.components.SolarizedColor;

public class FolderList<T> extends JList<T> {
	private static final long serialVersionUID = 5962590515579500161L;
	
	public FolderList(ListModel<T> dataModel) {
		super(dataModel);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellRenderer(new FolderListCellRendered());
		this.setBorder(null);
		this.setFixedCellHeight(24);
		
		this.setSelectionBackground(SolarizedColor.BLUE);
		this.setSelectionForeground(Color.WHITE);
	}

	private static class FolderListCellRendered extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		
		private static final Border baseBorder = new EmptyBorder(0, 5, 0, 5);
		private static final Border lineBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, SolarizedColor.BASE2); 

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			File folder = (File) value;

			JLabel component = (JLabel) super.getListCellRendererComponent(list, folder.getName(), index,
					isSelected, cellHasFocus);
			
			component.setOpaque(true);
			component.setToolTipText(folder.getAbsolutePath());
			
			if (index > 0) {
				component.setBorder(BorderFactory.createCompoundBorder(lineBorder, baseBorder));
			}
			else {
				component.setBorder(baseBorder);
			}
			
			return component;
		}
	}
}
