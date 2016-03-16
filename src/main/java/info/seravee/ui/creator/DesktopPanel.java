package info.seravee.ui.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import info.seravee.business.workers.SaveImageWorker;
import info.seravee.data.ScalingAlgorithm;
import info.seravee.data.Screen;
import info.seravee.wallcreator.business.workers.SetWallpaperWorker;
import info.seravee.wallcreator.ui.monitors.ScreensViewPanel;

public class DesktopPanel {
	private final JPanel desktopPanel;
	
	private final ScreensViewPanel screensViewPanel;
	
	private final JTabbedPane tabbedPane;
	
	private final JButton saveImageButton;
	private final JButton setImageButton;
	
	private final Map<Integer, DesktopParameterPanel> desktopParamsMap;
	
	public DesktopPanel() {
		desktopParamsMap = new HashMap<>();
		
		desktopPanel = new JPanel(new BorderLayout(5,5));
		
		screensViewPanel = new ScreensViewPanel();
		
		tabbedPane = new JTabbedPane();
		

        
        saveImageButton = new JButton("Save image");
        saveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (.png)", "png"));
                fc.setAcceptAllFileFilterUsed(false);

                int returnVal = fc.showSaveDialog(desktopPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    
                    if (!file.getName().endsWith(".png")) {
                        file = new File(file.getAbsolutePath() + ".png");
                    }

                    new SaveImageWorker(file, screensViewPanel.getData()).execute();
                }
            }
        });
        
        setImageButton = new JButton("Set wallpaper");
        setImageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new SetWallpaperWorker(screensViewPanel.getData()).execute();
			}
		});
	}
	
	public void buildPanel() {
		desktopPanel.add(screensViewPanel, BorderLayout.CENTER);
		
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		buttonsPanel.add(saveImageButton);
		buttonsPanel.add(setImageButton);

		JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(tabbedPane, BorderLayout.CENTER);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        desktopPanel.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	public JPanel getDisplay() {
		return desktopPanel;
	}
	
	public void setDesktopConfig(List<Screen> screenList) {
		desktopParamsMap.clear();
		for (Screen screen : screenList) {
            buildImageSelectPanel(screen);
        }
	}
	
	private void buildImageSelectPanel(Screen screen) {
        DesktopParameterPanel dpPanel = new DesktopParameterPanel();

        final JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dpPanel.build();
        panel.add(dpPanel.getDisplay(), BorderLayout.CENTER);

        tabbedPane.add("Desktop " + screen.getId(), panel);

        screensViewPanel.addScreen(screen);

        desktopParamsMap.put(screen.getId(), dpPanel);
        
        dpPanel.addPropertyChangeListener(new DesktopParamChangeListener(screen.getId(), screensViewPanel));
    }
	
	public void setWallpaper(File wallpaper, int desktopIndex) {
		desktopParamsMap.get(desktopIndex).setWallpaperFile(wallpaper);
		screensViewPanel.setImage(desktopIndex, wallpaper);
	}
	
	private class DesktopParamChangeListener implements PropertyChangeListener {
		
		private final int desktopIndex;
		private final ScreensViewPanel screensViewPanel;
		
		public DesktopParamChangeListener(final int desktopIndex, final ScreensViewPanel screensViewPanel) {
			this.desktopIndex = desktopIndex;
			this.screensViewPanel = screensViewPanel;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("file".equals(evt.getPropertyName())) {
				screensViewPanel.setImage(desktopIndex, (File) evt.getNewValue());
			}
			else if ("scalingAlgorithm".equals(evt.getPropertyName())) {
				screensViewPanel.setScalingAlgorithm(desktopIndex, (ScalingAlgorithm) evt.getNewValue());
			}
			else if ("bgColor".equals(evt.getPropertyName())) {
				screensViewPanel.setBackgroundColor(desktopIndex, (Color) evt.getNewValue());
			}
		}
	}
}
