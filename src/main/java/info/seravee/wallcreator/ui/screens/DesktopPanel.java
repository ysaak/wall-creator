package info.seravee.wallcreator.ui.screens;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import info.seravee.business.config.ConfigurationManager;
import info.seravee.utils.SwingUtils;
import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.business.Services;
import info.seravee.wallcreator.business.workers.DuplicateProfileWorker;
import info.seravee.wallcreator.business.workers.RestoreProfileWorker;
import info.seravee.wallcreator.business.workers.SaveImageWorker;
import info.seravee.wallcreator.business.workers.StoreProfileWorker;
import info.seravee.wallcreator.events.SelectedProfileEvent;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallcreator.ui.components.SolarizedColor;

public class DesktopPanel {
	private final JPanel desktopPanel;
	
	private final ScreensViewPanel screensViewPanel;
	
	private final WallpaperParametersPanel parametersPanel;
	
	private final DefaultComboBoxModel<Profile> profilesModel;
	private final JComboBox<Profile> profilesList;
	
	private final JButton saveImageButton;
	
	private final JButton saveButton;
	private final JButton saveAndSetButton;
	private final JButton cancelButton;
	
	private final JButton duplicateProfileButton;
	
	private boolean updateInProgress = false;
	
	public DesktopPanel() {
		desktopPanel = new JPanel();
		
		// Profiles list
		profilesModel = new DefaultComboBoxModel<>();
		profilesList = new JComboBox<>(profilesModel);
		profilesList.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -4083147274728881969L;

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				String text = "";
				if (value != null && value instanceof Profile) {
					text = ((Profile) value).getName();
				}
				
				return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
			}
		});
		profilesList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fireProfileSelected((Profile) profilesList.getSelectedItem());
			}
		});
		
		// Screen vies
		screensViewPanel = new ScreensViewPanel();
		
		// Parameters
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

                    new SaveImageWorker(file, (Profile) profilesList.getSelectedItem()).execute();
                }
            }
        });
        
        // Buttons
        saveAndSetButton = new JButton("Save and set");
        saveAndSetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new StoreProfileWorker((Profile) profilesList.getSelectedItem(), true).execute();
			}
		});
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new StoreProfileWorker((Profile) profilesList.getSelectedItem(), false).execute();
			}
		});
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RestoreProfileWorker((Profile) profilesList.getSelectedItem()).execute();
			}
		});
        
        
        
        /*------*/
        duplicateProfileButton = new JButton("Duplicate");
        duplicateProfileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				final String newProfileName = JOptionPane.showInputDialog(getDisplay(), "Name", "Enter new profile name", JOptionPane.QUESTION_MESSAGE);
				if (newProfileName != null) {
					new DuplicateProfileWorker((Profile) profilesList.getSelectedItem(), newProfileName).execute();
				}
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
		gbc.addAnchoredComponent(saveImageButton, 0, 0, GridBagConstraints.LINE_START);
		gbc.addComponent(Box.createHorizontalGlue(), 1, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addAnchoredComponent(saveButton, 2, 0, GridBagConstraints.LINE_START);
		gbc.addAnchoredComponent(saveAndSetButton, 3, 0, GridBagConstraints.LINE_START);
		gbc.addAnchoredComponent(cancelButton, 4, 0, GridBagConstraints.LINE_START);

		SwingUtils.setSMPSizes(screensViewPanel, new Dimension(screensViewPanel.getPreferredSize().width, 225));
		
		gbc = new GBCHelper(desktopPanel);
		gbc.addComponent(buildProfilePanel(), 0, 0, 0.0, 0.0, 2, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
        gbc.addComponent(screensViewPanel, 0, 1, 1.0, 1.0, 2, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.BOTH);
		gbc.addComponent(parametersPanel.getDisplay(), 0, 2, 1.0, 1.5, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.BOTH);
		gbc.addComponent(buttonsPanel, 0, 3, 0.0, 0.0, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        
        
        gbc.addComponent(Box.createHorizontalGlue(), 1, 2, 3.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GBCHelper.DEFAULT_FILL);
	}
	
	private JPanel buildProfilePanel() {
		JPanel profilePanel = new JPanel();
		profilePanel.setOpaque(false);
		
		GBCHelper gbc = new GBCHelper(profilePanel);
		gbc.addComponent(profilesList, 0, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addAnchoredComponent(duplicateProfileButton, 1, 0, GridBagConstraints.LINE_START);

		
		return profilePanel;
	}
	
	public JPanel getDisplay() {
		return desktopPanel;
	}
	
	public void setProfiles(List<Profile> profiles) {
		Profile selectedProfile = null;
		
		String selectedProfileId = ConfigurationManager.get().getSelectedProfile();
		
		for (Profile p : profiles) {
			profilesModel.addElement(p);
			
			if ((selectedProfileId == null && selectedProfile == null) || p.getId().equals(selectedProfileId)) {
				selectedProfile = p;
			}
		}
		
		if (selectedProfile == null) {
			selectedProfile = profiles.get(0);
		}

		fireProfileSelected(selectedProfile);
		
	}
	
	protected void fireProfileSelected(final Profile profile) {
		if (!updateInProgress)
			Services.getEventService().post(new SelectedProfileEvent(profile));
		//setSelectedProfile(profile);
	}
	
	public void profileAdded(Profile profile) {
		profilesModel.addElement(profile);
	}
	
	public void profileSelected(Profile profile) {
		updateInProgress = true;
		profilesList.setSelectedItem(profile);
		screensViewPanel.setScreens(profile.getScreens());
		screensViewPanel.setSelectedScreen(profile.getScreens().get(0));
		parametersPanel.setCurrentScreen(profile.getScreens().get(0));
		updateInProgress = false;
	}
}
