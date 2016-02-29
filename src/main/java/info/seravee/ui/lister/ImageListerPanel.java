package info.seravee.ui.lister;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import info.seravee.DefaultConfiguration;
import info.seravee.data.lister.Wallpaper;
import info.seravee.ui.WallpaperSelectionListener;
import info.seravee.ui.lister.ImageLoadingWorker.LoadingWorkerListener;
import info.seravee.utils.SwingUtils;

public class ImageListerPanel {
	
	private final DefaultListModel<File> folderListModel;
	private final JList<File> folderList;
	
	private final DefaultListModel<Wallpaper> imageListModel;
	private final JList<Wallpaper> imageList;
	
	private final JPanel listerPanel;
	
	private final JPopupMenu popup;
	
	private WallpaperSelectionListener wallpaperSelectionListener = null;
	
	private final JButton addFolderButton;
	private final JButton removeFolderButton;
	
	
	private ImageLoadingWorker worker = null;
	private final LoadingWorkerListener workerListener;
	
	public ImageListerPanel() {
		listerPanel = new JPanel(new BorderLayout());
		
		imageListModel = new DefaultListModel<>();
		imageList = new JList<>(imageListModel);
		imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		imageList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		imageList.setVisibleRowCount(-1);
		imageList.setCellRenderer(new ListCellRenderer<Wallpaper>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Wallpaper> list, Wallpaper value, int index,
					boolean isSelected, boolean cellHasFocus) {
				
				if (value.getImage() != null) {
					return new ThumbnailView(value.getFile().getName(), new ImageIcon(value.getImage()), isSelected);
				}
				
				return new JLabel(value.getFile().getName());
			}
		});
		
		imageList.setBackground(Color.GRAY);
		
        //Create the popup menu.
        popup = new JPopupMenu();
        popup.add(new JMenuItem(new SetToDesktopAction(1)));
        popup.add(new JMenuItem(new SetToDesktopAction(2)));
        
        imageList.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		triggerPopupMenu(e);
        	}
        	
        	@Override
        	public void mouseReleased(MouseEvent e) {
        		triggerPopupMenu(e);
        	}
        	
        	private void triggerPopupMenu(MouseEvent e) {
        		if (e.isPopupTrigger()) {
        			imageList.setSelectedIndex(imageList.locationToIndex(e.getPoint()));
        			
        			if (imageList.getSelectedIndex() != -1)
        				popup.show(imageList, e.getX(), e.getY());
        		}
        	}
		});
        
        // Folders
        folderListModel = new DefaultListModel<>();
        folderList = new JList<>(folderListModel);
        folderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        folderList.setVisibleRowCount(-1);
        folderList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && folderList.getSelectedIndex() != -1) {
					System.err.println(">> " + folderList.getSelectedValue());
					startImageLoading(folderList.getSelectedValue());
				}
			}
		});
        folderList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
        	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		File folder = (File) value;
        		
				JLabel component = (JLabel) super.getListCellRendererComponent(list, folder.getName(), index, isSelected, cellHasFocus);
				component.setToolTipText(folder.getAbsolutePath());
        		return component;
        	}
		});
        
        addFolderButton = new JButton("+");
        removeFolderButton = new JButton("-");
        
        folderListModel.addElement(new File("C:\\Users\\ROTHDA\\Pictures\\wp"));
        folderListModel.addElement(new File("C:\\Users\\ROTHDA\\Pictures\\wp\\Accel World"));
        
        // Worker listener
        workerListener = new LoadingWorkerListener() {
			@Override
			public void wallpaperDetected(Wallpaper wallpaper) {
				imageListModel.addElement(wallpaper);
			}
		};
	}
	
	
	public void buildPanel() {
		listerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		// -- Folder panel
		SwingUtils.setSMPSizes(folderList, new Dimension(DefaultConfiguration.FOLDER_LIST_WIDTH, DefaultConfiguration.FOLDER_LIST_HEIGHT));
		
		final JPanel folderPanel = new JPanel(new BorderLayout());
		
		final JScrollPane folderScroller = new JScrollPane(folderList);
		folderScroller.setBackground(Color.WHITE);
		
		folderPanel.add(folderScroller, BorderLayout.CENTER);
		
		JPanel folderButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		folderButtonsPanel.add(addFolderButton);
		folderButtonsPanel.add(removeFolderButton);
		
		folderPanel.add(folderButtonsPanel, BorderLayout.SOUTH);
		
		listerPanel.add(folderPanel, BorderLayout.WEST);
		
		// -- Thumbnail list panel
		JScrollPane listScroller = new JScrollPane(imageList);
        listScroller.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        listScroller.setBackground(Color.GRAY);
        listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Compute component width
        Dimension viewDimension = new Dimension(
        		// Don't forget to get scrollbar size
        		ThumbnailView.getViewWidth() * DefaultConfiguration.THUMBNAIL_PER_LINE, 
        		ThumbnailView.getViewHeight() * DefaultConfiguration.THUMBNAIL_LINES
        );
        
        SwingUtils.setSMPSizes(listScroller.getViewport(), viewDimension);

        listerPanel.add(listScroller, BorderLayout.CENTER);
	}
	
	public JComponent getDisplay() {
		return listerPanel;
	}
	
	protected void startImageLoading(File folder) {
		if (worker != null) {
			worker.removeListener();
			worker.cancel(true);
		}
		
		imageListModel.clear();
		worker = new ImageLoadingWorker(folder, workerListener);
		worker.execute();
	}
	
	public void setWallpaperSelectionListener(WallpaperSelectionListener wallpaperSelectionListener) {
		this.wallpaperSelectionListener = wallpaperSelectionListener;
	}
	
	private static class ThumbnailView extends JPanel {
		private static final long serialVersionUID = -3824902428937556064L;
		
		private static final Dimension imageDimension = new Dimension(DefaultConfiguration.THUMBNAIL_WIDTH, DefaultConfiguration.THUMBNAIL_HEIGHT);
		
		private final JLabel imageLabel;
		
		public ThumbnailView(String text, ImageIcon image, boolean selected) {
			setOpaque(false);
			this.setLayout(new BorderLayout(0,0));
			if (selected) {
				this.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(3,3,3,3), 
						BorderFactory.createLineBorder(Color.BLUE, 2)
				));
			}
			else {
				this.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			}

			imageLabel = new JLabel(image);
			imageLabel.setSize(imageDimension);
			imageLabel.setMinimumSize(imageDimension);
			imageLabel.setPreferredSize(imageDimension);
			imageLabel.setMaximumSize(imageDimension);
			
			this.setToolTipText(text);
			
			this.add(imageLabel, BorderLayout.CENTER);
		}
		
		public static final int getViewWidth() {
			return imageDimension.width + (5 * 2);
		}
		
		public static final int getViewHeight() {
			return imageDimension.height + (5 * 2);
		}
	}
	
	private class SetToDesktopAction extends AbstractAction {
		private static final long serialVersionUID = -3408983078331514079L;
		private final int desktopIndex;
		
		public SetToDesktopAction(final int desktopIndex) {
			super("Set to desktop " + desktopIndex);
			this.desktopIndex = desktopIndex;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (wallpaperSelectionListener != null) {
				wallpaperSelectionListener.wallpaperSelected(imageList.getSelectedValue(), desktopIndex);
			}
		}
	}

	public void setDesktopConfig(List<Rectangle> config) {
		// TODO init popup menu with the good number of screens
		
	}
}
