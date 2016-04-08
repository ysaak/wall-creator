package info.seravee.wallmanager.ui.frame.desktop;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.google.common.eventbus.Subscribe;

import info.seravee.utils.SwingUtils;
import info.seravee.wallcreator.business.workers.RestoreProfileWorker;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallcreator.ui.components.SolarizedColor;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.business.Services;
import info.seravee.wallmanager.business.events.EventBusLine;
import info.seravee.wallmanager.business.workers.SaveImageWorker;
import info.seravee.wallmanager.business.workers.StoreProfileWorker;
import info.seravee.wallmanager.ui.frame.events.ProfileSelectedEvent;
import info.seravee.wallmanager.ui.frame.events.ProfileVersionSelectedEvent;
import info.seravee.wallmanager.ui.frame.events.ScreenSelectedEvent;

public class DesktopEditorPanel {
	private final JPanel desktopPanel;
	
	private final ScreensViewPanel screensViewPanel;
	private final WallpaperParametersPanel parametersPanel;
	
	private final JButton saveImageButton;
	
	private final JButton saveButton;
	private final JButton saveAndSetButton;
	private final JButton cancelButton;
	
	private final ScreenListener screenListener;
	
	private Profile currentProfile = null;
	private ProfileVersion currentVersion = null;
	
	public DesktopEditorPanel() {
		desktopPanel = new JPanel();
		
		// Screen vies
		screensViewPanel = new ScreensViewPanel();
		
		// Parameters
		parametersPanel = new WallpaperParametersPanel();
        
        saveImageButton = new JButton("Save image");
        saveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setAcceptAllFileFilterUsed(false);
                fc.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (.png)", "png"));

                int returnVal = fc.showSaveDialog(desktopPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    
                    if (!file.getName().endsWith(".png")) {
                        file = new File(file.getAbsolutePath() + ".png");
                    }

                    new SaveImageWorker(file, currentProfile, currentVersion).execute();
                }
            }
        });
        
        screenListener = new ScreenListener() {
			
			@Override
			public void screenSelected(Screen screen) {
				//handleScreenSelected(screen);
			}
			
			@Override
			public void screenParametersUpdated(WallpaperParameters params) {
				screensViewPanel.rebuildScreenImage(params.getScreenId());
			}
		};
		screensViewPanel.addScreenListener(screenListener);
        parametersPanel.addScreenListener(screenListener);
		
        // Buttons
        saveAndSetButton = new JButton("Save and set");
        saveAndSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new StoreProfileWorker(currentProfile, currentVersion).execute();
			}
		});
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new StoreProfileWorker(currentProfile, null).execute();
			}
		});
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// FIXME : descendre d'un niveau
				RestoreProfileWorker worker = new RestoreProfileWorker(currentProfile);
				worker.execute();
			}
		});
        
        
        Services.getEventService().register(EventBusLine.FRAME, this);
	}
	
	public void buildPanel() {
		desktopPanel.setOpaque(false);
		
		parametersPanel.build();
		parametersPanel.getDisplay().setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SolarizedColor.BASE2));
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		buttonsPanel.setOpaque(false);
		
		GBCHelper gbc = new GBCHelper(buttonsPanel);
		gbc.addAnchoredComponent(saveImageButton, 0, 0, GridBagConstraints.LINE_START);
		gbc.addComponent(Box.createHorizontalGlue(), 1, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addAnchoredComponent(saveButton, 2, 0, GridBagConstraints.LINE_START);
		gbc.addAnchoredComponent(saveAndSetButton, 3, 0, GridBagConstraints.LINE_START);
		gbc.addAnchoredComponent(cancelButton, 4, 0, GridBagConstraints.LINE_START);

		SwingUtils.setSMPSizes(screensViewPanel, new Dimension(screensViewPanel.getPreferredSize().width, 225));
		
		gbc = new GBCHelper(desktopPanel);
        gbc.addComponent(screensViewPanel, 0, 0, 1.0, 1.0, 2, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.BOTH);
		gbc.addComponent(parametersPanel.getDisplay(), 0, 1, 1.0, 1.5, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.BOTH);
		gbc.addComponent(buttonsPanel, 0, 2, 0.0, 0.0, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        
        
        gbc.addComponent(Box.createHorizontalGlue(), 1, 2, 3.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GBCHelper.DEFAULT_FILL);
	}
	
	public JPanel getDisplay() {
		return desktopPanel;
	}
	
	@Subscribe
	public void profileSelected(ProfileSelectedEvent event) {
		if (event.getProfile().equals(currentProfile))
			return;
		
		currentProfile = event.getProfile();
		
		// Display configuration
		screensViewPanel.setScreens(currentProfile.getConfiguration());
		
		// Select the first screen --> FIXME move to ScreenViewPanel
		Screen selectedScreen = null; 
		for (Screen screen : currentProfile.getConfiguration()) {
			if (selectedScreen == null || (screen.getX() <= selectedScreen.getX() && screen.getY() <= selectedScreen.getY())) {
				selectedScreen = screen;
			}
		}
		
		screensViewPanel.setSelectedScreen(selectedScreen);
	}
	
	@Subscribe
	public void profileVersionSelected(ProfileVersionSelectedEvent event) {
		
		if (!event.getProfile().equals(currentProfile)) {
			profileSelected(new ProfileSelectedEvent(event.getProfile()));
		}
		else if (event.getVersion().equals(currentVersion)) {
			return;
		}
		
		currentVersion = event.getVersion();
		
		// Push version to screenview 
		screensViewPanel.setProfileVersion(currentVersion);
		
		// Update parameter field with currently selected panel
		final Screen selectedScreen = screensViewPanel.getSelectedScreen();
		parametersPanel.setCurrentScreen(currentVersion.getScreenParameters(selectedScreen.getId()));
	}
	
	@Subscribe
	public void screenSelected(ScreenSelectedEvent event) {
		if (currentVersion != null) {
			parametersPanel.setCurrentScreen(currentVersion.getScreenParameters(event.getScreen().getId()));
		}
	}
	
	public void wallpaperSelectedForScreen(int screenId, File imageFile) {
		if (currentVersion != null) {
			final WallpaperParameters params = currentVersion.getScreenParameters(screenId);
			params.setImage(imageFile != null ? imageFile.getAbsolutePath() : null);
			
			screensViewPanel.rebuildScreenImage(screenId);
		}
	}
}
