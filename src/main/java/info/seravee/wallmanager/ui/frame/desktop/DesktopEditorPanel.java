package info.seravee.wallmanager.ui.frame.desktop;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import info.seravee.utils.SwingUtils;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallcreator.ui.components.SolarizedColor;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.ui.frame.events.ScreenSelectedEvent;

public class DesktopEditorPanel {
	private final JPanel desktopPanel;
	
	private final ScreensViewPanel screensViewPanel;
	private final WallpaperParametersPanel parametersPanel;
	
	private final JButton saveButton;
	private final JButton saveAndSetButton;
	private final JButton cancelButton;
	
	private final ScreenListener screenListener;
	private ProfileEditorListener listener = null;
	
	private Profile currentProfile = null;
	private ProfileVersion currentVersion = null;
	
	public DesktopEditorPanel() {
		desktopPanel = new JPanel();
		
		// Screen views
		screensViewPanel = new ScreensViewPanel();
		
		// Parameters
		parametersPanel = new WallpaperParametersPanel();
        
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
				if (listener != null) {
					listener.storeProfile(currentProfile, currentVersion, true);
				}
			}
		});
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null) {
					listener.storeProfile(currentProfile, currentVersion, false);
				}
			}
		});
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listener != null)
					listener.resetProfileVersion(currentProfile, currentVersion);
			}
		});
	}
	
	public void buildPanel() {
		desktopPanel.setOpaque(false);
		
		parametersPanel.build();
		parametersPanel.getDisplay().setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SolarizedColor.BASE2));
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		buttonsPanel.setOpaque(false);
		
		GBCHelper gbc = new GBCHelper(buttonsPanel);
		gbc.addComponent(Box.createHorizontalGlue(), 0, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addAnchoredComponent(saveButton, 1, 0, GridBagConstraints.LINE_START);
		gbc.addAnchoredComponent(saveAndSetButton, 2, 0, GridBagConstraints.LINE_START);
		gbc.addAnchoredComponent(cancelButton, 3, 0, GridBagConstraints.LINE_START);

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
	
	public void profileSelected(Profile profile) {
		if (profile.equals(currentProfile))
			return;
		
		currentProfile = profile;
		
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
	
	public void profileVersionSelected(Profile profile, ProfileVersion version) {
		
		if (!profile.equals(currentProfile)) {
			profileSelected(profile);
		}
		else if (version.equals(currentVersion)) {
			return;
		}
		
		currentVersion = version;
		
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
	
	public void setListener(ProfileEditorListener listener) {
		this.listener = listener;
	}
}
