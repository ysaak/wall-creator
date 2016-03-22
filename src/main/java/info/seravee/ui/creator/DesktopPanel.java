package info.seravee.ui.creator;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import info.seravee.business.workers.SaveImageWorker;
import info.seravee.utils.SwingUtils;
import info.seravee.wallcreator.beans.Screen;
import info.seravee.wallcreator.business.workers.SetWallpaperWorker;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallcreator.ui.components.SolarizedColor;
import info.seravee.wallcreator.ui.screens.ScreensViewPanel;
import info.seravee.wallcreator.ui.screens.WallpaperParametersPanel;

public class DesktopPanel {
	private final JPanel desktopPanel;
	
	private final ScreensViewPanel screensViewPanel;
	
	private final WallpaperParametersPanel parametersPanel;
	
	private final JButton saveImageButton;
	private final JButton setImageButton;
	
	public DesktopPanel() {
		desktopPanel = new JPanel();
		
		screensViewPanel = new ScreensViewPanel();
		
		parametersPanel = new WallpaperParametersPanel();
		screensViewPanel.addScreenListener(parametersPanel);
        
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
		desktopPanel.setOpaque(false);
		
		parametersPanel.build();
		parametersPanel.getDisplay().setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SolarizedColor.BASE2));
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		buttonsPanel.setOpaque(false);
		buttonsPanel.add(saveImageButton);
		buttonsPanel.add(setImageButton);

		SwingUtils.setSMPSizes(screensViewPanel, new Dimension(screensViewPanel.getPreferredSize().width, 225));
		
		final GBCHelper gbc = new GBCHelper(desktopPanel);
        gbc.addComponent(screensViewPanel, 0, 0, 1.0, 1.0, 2, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.BOTH);
		gbc.addComponent(parametersPanel.getDisplay(), 0, 1, 1.0, 1.5, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.BOTH);
        gbc.addAnchoredComponent(buttonsPanel, 1, 2, GridBagConstraints.LINE_END);
        
        
        gbc.addComponent(Box.createHorizontalGlue(), 1, 1, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GBCHelper.DEFAULT_FILL);
	}
	
	public JPanel getDisplay() {
		return desktopPanel;
	}
	
	public void setDesktopConfig(List<Screen> screenList) {
		screensViewPanel.setScreens(screenList);
		screensViewPanel.setSelectedScreen(screenList.get(0));
		parametersPanel.setCurrentScreen(screenList.get(0));
	}
}
