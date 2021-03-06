package wallmanager.ui.frame.wallpapers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import wallmanager.DefaultConfiguration;
import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.Screen;
import wallmanager.ui.GuiConstants;
import wallmanager.ui.commons.DropShadowBorder;
import wallmanager.ui.commons.SolarizedColor;
import wallmanager.ui.commons.component.LightScrollPane;
import wallmanager.ui.commons.component.xbutton.XButton;
import wallmanager.ui.commons.component.xbutton.XButtonPosition;
import wallmanager.ui.frame.SwingUtils;
import wallmanager.ui.frame.wallpapers.ImageLoadingWorker.LoadingWorkerListener;
import wallmanager.ui.icons.MinusIcon;
import wallmanager.ui.icons.PlusIcon;

public class WallpapersListPanel {

	private final DefaultListModel<File> folderListModel;
	private final JList<File> folderList;
	private final JLabel folderLabel;

	private final DefaultListModel<Wallpaper> imageListModel;
	private final JList<Wallpaper> imageList;

	private final JPanel listerPanel;

	private final XButton addFolderButton;
	private final XButton removeFolderButton;

	private ImageLoadingWorker worker = null;
	private final LoadingWorkerListener workerListener;
	
	private Profile selectedProfile = null;
	
	private final Set<WallpapersListListener> wallpaperListListener;

	public WallpapersListPanel() {
		wallpaperListListener = new HashSet<>();
		
		listerPanel = new JPanel(new BorderLayout(10,10));

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

		imageList.setBackground(Color.WHITE);

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
						
					createImageListPopupMenu().show(imageList, e.getX(), e.getY());
				}
			}
		});

		// Folders
		folderLabel = new JLabel("Folders :");
		folderLabel.setFont(folderLabel.getFont().deriveFont(Font.BOLD));
		
		folderListModel = new DefaultListModel<>();
		folderList = new FolderList<>(folderListModel);
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

		addFolderButton = new XButton(new AddDirectoryAction(), XButtonPosition.FIRST);
		removeFolderButton = new XButton(new RemoveFolderAction(), XButtonPosition.LAST);

		// Worker listener
		workerListener = new LoadingWorkerListener() {
			@Override
			public void wallpaperDetected(Wallpaper wallpaper) {
				imageListModel.addElement(wallpaper);
			}
		};
	}

	public void buildPanel() {
		listerPanel.setOpaque(false);

		// -- Folder panel
		final JPanel folderPanel = new JPanel(new BorderLayout(0,0));
		folderPanel.setBackground(Color.WHITE);
		folderPanel.setBorder(new CompoundBorder(
				new MatteBorder(0, 0, 0, 1, SolarizedColor.BASE2),
				new EmptyBorder(0, 0, 0, GuiConstants.BASE_SPACER)
		));
		
		folderLabel.setBorder(new EmptyBorder(GuiConstants.SMALL_SPACER, 0, GuiConstants.SMALL_SPACER, 0));
		

		final LightScrollPane folderScroller = new LightScrollPane(folderList);
		folderScroller.setBackground(Color.WHITE);
		folderScroller.setBorder(null);

		folderPanel.add(folderLabel, BorderLayout.NORTH);
		folderPanel.add(folderScroller, BorderLayout.CENTER);

		JPanel folderButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		folderButtonsPanel.setOpaque(false);
		folderButtonsPanel.add(addFolderButton);
		folderButtonsPanel.add(removeFolderButton);

		folderPanel.add(folderButtonsPanel, BorderLayout.SOUTH);

		listerPanel.add(folderPanel, BorderLayout.WEST);

		// -- Thumbnail list panel
		LightScrollPane listScroller = new LightScrollPane(imageList);
		listScroller.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
		listScroller.setBackground(Color.GRAY);
		listScroller.setBorder(null);

		// Compute component width
		Dimension viewDimension = new Dimension(
				// Don't forget to get scrollbar size
				ThumbnailView.getViewWidth() * DefaultConfiguration.THUMBNAIL_PER_LINE + listScroller.getScrollBarSize(),
				ThumbnailView.getViewHeight() * DefaultConfiguration.THUMBNAIL_LINES);

		SwingUtils.setSMPSizes(listScroller, viewDimension);
		
		SwingUtils.setSMPSizes(folderPanel,
				new Dimension(DefaultConfiguration.FOLDER_LIST_WIDTH, DefaultConfiguration.FOLDER_LIST_HEIGHT));

		listerPanel.add(listScroller, BorderLayout.CENTER);
	}

	public JComponent getDisplay() {
		return listerPanel;
	}

	protected JPopupMenu createImageListPopupMenu() {
		final JPopupMenu popup = new JPopupMenu();
		
		if (selectedProfile != null && selectedProfile.getConfiguration() != null) {
			for (Screen screen : selectedProfile.getConfiguration())
				popup.add(new JMenuItem(new SetToDesktopAction(screen.getId())));
		}
		
		
		return popup;
	}
	
	protected void startImageLoading(File folder) {
		if (worker != null) {
			worker.removeListener();
			worker.cancel(true);
		}

		imageListModel.clear();
		if (folder != null) {
			worker = new ImageLoadingWorker(folder, workerListener);
			worker.execute();
		}
	}

	public void setSelectedProfile(final Profile profile) {
		this.selectedProfile = profile;
	}

	protected void fireWallpaperSelectedChanged(final int screenId, final File imageFile) {
		synchronized (wallpaperListListener) {
			for (WallpapersListListener l : wallpaperListListener) {
				l.wallpaperSelectedForScreen(screenId, imageFile);
			}
		}
	}
	
	protected void fireAddFolderEvent() {
		synchronized (wallpaperListListener) {
			for (WallpapersListListener l : wallpaperListListener) {
				l.addFolderEvent(null);
			}
		}
	}
	
	protected void fireRemoveFolderEvent(final File folder) {
		synchronized (wallpaperListListener) {
			for (WallpapersListListener l : wallpaperListListener) {
				l.removeFolderEvent(folder);
			}
		}
	}
	
	public void addWallpapersListListener(WallpapersListListener l) {
		synchronized (wallpaperListListener) {
			wallpaperListListener.add(l);
		}
	}
	
	public void removeWallpapersListListener(WallpapersListListener l) {
		synchronized (wallpaperListListener) {
			wallpaperListListener.remove(l);
		}
	}

	public void setFolders(List<String> folders) {
		folderListModel.removeAllElements();
		
		for(String folder : folders) {
			folderListModel.addElement(new File(folder));
		}
	}

	public void addFolder(File folder) {
		folderListModel.addElement(folder);
	}
	
	private static class ThumbnailView extends JPanel {
		private static final long serialVersionUID = -3824902428937556064L;

		private static final Dimension imageDimension = new Dimension(DefaultConfiguration.THUMBNAIL_WIDTH,
				DefaultConfiguration.THUMBNAIL_HEIGHT);

		private final JLabel imageLabel;

		public ThumbnailView(String text, ImageIcon image, boolean selected) {
			setOpaque(false);
			this.setLayout(new BorderLayout(0, 0));
			this.setBorder(getBorder(selected));

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
		
		private Border getBorder(boolean selected) {
			DropShadowBorder shadow = new DropShadowBorder(Color.BLACK, 5);
	        shadow.setShowLeftShadow(true);
	        shadow.setShowRightShadow(true);
	        shadow.setShowBottomShadow(true);
	        shadow.setShowTopShadow(true);
	        return shadow;
		}
	}

	private class SetToDesktopAction extends AbstractAction {
		private static final long serialVersionUID = -3408983078331514079L;
		private final int screenId;

		public SetToDesktopAction(final int screenId) {
			super("Set to desktop " + screenId);
			this.screenId = screenId;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fireWallpaperSelectedChanged(screenId, imageList.getSelectedValue().getFile());
		}
	}

	private class AddDirectoryAction extends AbstractAction {
		private static final long serialVersionUID = 3830971456460167481L;

		public AddDirectoryAction() {
			super("", new PlusIcon(14));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fireAddFolderEvent();
		}
	}
	
	private class RemoveFolderAction extends AbstractAction {
		private static final long serialVersionUID = -3159823531107055372L;

		public RemoveFolderAction() {
			super("", new MinusIcon(14));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			File selectedValue = folderList.getSelectedValue();
			if (selectedValue != null) {
				folderListModel.removeElement(selectedValue);
				fireRemoveFolderEvent(selectedValue);
				
				// TODO : vider la liste des images
			}
		}
	}
}
