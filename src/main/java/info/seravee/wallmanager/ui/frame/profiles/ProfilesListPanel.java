package info.seravee.wallmanager.ui.frame.profiles;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.business.Services;
import info.seravee.wallmanager.business.events.EventBusLine;
import info.seravee.wallmanager.business.workers.StoreProfileWorker;
import info.seravee.wallmanager.ui.commons.components.JXPanel;
import info.seravee.wallmanager.ui.frame.events.ProfileSelectedEvent;
import info.seravee.wallmanager.ui.frame.events.ProfileVersionSelectedEvent;

public final class ProfilesListPanel {
	private final JXPanel panel;

	private final JLabel profileLabel;
	private final DefaultComboBoxModel<Profile> profilesModel;
	private final JComboBox<Profile> profilesList;

	private final JLabel versionLabel;
	private final DefaultComboBoxModel<ProfileVersion> profilesVersionModel;
	private final JComboBox<ProfileVersion> profilesVersionList;
	
	
	private final JButton profileRenameButton;
	
	private final JButton versionAddButton;
	private final JButton versionRenameButton; 
	private final JButton versionSetPreferredButton;
	private final JButton versionDeleteButton;

	public ProfilesListPanel() {
		this.panel = new JXPanel().allRounded();

		// Profiles list
		profileLabel = new JLabel("Profile");
		
		profilesModel = new DefaultComboBoxModel<>();
		profilesList = new JComboBox<>(profilesModel);
		profilesList.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -4083147274728881969L;

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
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
		
		profileRenameButton = new JButton("Rename");
		profileRenameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final Profile p = getSelectedProfile();
				
				Object newName = (String) JOptionPane.showInputDialog(panel, "New name", "Rename profile", JOptionPane.QUESTION_MESSAGE, null, null, p.getName());
				
				if (newName != null) {
					p.setName((String) newName);
					
					new StoreProfileWorker(p, null).execute();
					
					panel.repaint();
				}
			}
		});
		
		/* --------------------------------------------------------------------- */

		// Profile's versions
		versionLabel = new JLabel("Version");
		
		profilesVersionModel = new DefaultComboBoxModel<>();
		profilesVersionList = new JComboBox<>(profilesVersionModel);
		profilesVersionList.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -4083147274728881969L;

			@SuppressWarnings("rawtypes")
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String text = "";
				if (value != null && value instanceof ProfileVersion) {
					text = ((ProfileVersion) value).getName();
				}

				return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
			}
		});
		profilesVersionList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireProfileVersionSelected((ProfileVersion) profilesVersionList.getSelectedItem());
			}
		});
		
		versionSetPreferredButton = new JButton("Preferred");
		versionSetPreferredButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final Profile p = getSelectedProfile();
				final ProfileVersion pv = getSelectedVersion();
				
				try {
					Services.getProfileService().setPreferredVersion(p, pv);
					
					
					versionSetPreferredButton.setEnabled(false);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		versionRenameButton = new JButton("Rename");
		versionRenameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final ProfileVersion p = getSelectedVersion();
				
				Object newName = (String) JOptionPane.showInputDialog(panel, "New name", "Rename profile version", JOptionPane.QUESTION_MESSAGE, null, null, p.getName());
				
				if (newName != null) {
					p.setName((String) newName);
					
					new StoreProfileWorker(getSelectedProfile(), null).execute();
					
					panel.repaint();
				}
			}
		});
		
		versionAddButton = new JButton("Add");
		versionAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object name = (String) JOptionPane.showInputDialog(panel, "Name", "New version", JOptionPane.QUESTION_MESSAGE);
				
				if (name != null) {
					
					ProfileVersion version = new ProfileVersion();
					version.setName((String) name);
					
					
					Profile p = getSelectedProfile();
					
					List<WallpaperParameters> params = new ArrayList<>();
					
					for (Screen s : p.getConfiguration()) {
						params.add(new WallpaperParameters(s.getId()));
					}
					
					version.setParameters(params);
					
					p.getVersions().add(version);
					
					new StoreProfileWorker(p, null).execute();
					
					panel.repaint();
					
					profilesVersionModel.addElement(version);
					profilesVersionList.setSelectedItem(version);
				}
			}
		});
		
		versionDeleteButton = new JButton("Delete");
		versionDeleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final Profile profile = getSelectedProfile();
				final ProfileVersion version = getSelectedVersion();
				
				final int res = JOptionPane.showConfirmDialog(panel, "Confirm version removal", "Version remove", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.OK_OPTION) {
					
					profile.getVersions().remove(version);
					new StoreProfileWorker(profile, null).execute();
					
					profilesVersionModel.removeElement(version);
					fireProfileSelected(profile);
				}
			}
		});
	}

	public void buildPanel() {
		panel.setBackground(Color.WHITE);
		
		GBCHelper gbc = new GBCHelper(panel);
		gbc.addComponent(profileLabel, 0, 0);
		gbc.addComponent(profilesList, 1, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addComponent(versionLabel, 2, 0);
		gbc.addComponent(profilesVersionList, 3, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		
		
		gbc.addSpanningComponent(buildProfileButtonPanel(), 0, 1, 2, 1);
		gbc.addSpanningComponent(buildVersionButtonPanel(), 2, 1, 2, 1);
	}
	
	private JPanel buildProfileButtonPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		buttonsPanel.setOpaque(false);
		
		buttonsPanel.add(profileRenameButton);
		
		return buttonsPanel;
	}
	
	private JPanel buildVersionButtonPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		buttonsPanel.setOpaque(false);
		
		buttonsPanel.add(versionSetPreferredButton);
		buttonsPanel.add(versionRenameButton);
		buttonsPanel.add(new JSeparator(JSeparator.VERTICAL));
		buttonsPanel.add(versionAddButton);
		buttonsPanel.add(versionDeleteButton);
		
		return buttonsPanel;
	}

	public JPanel getDisplay() {
		return panel;
	}
	
	public Profile getSelectedProfile() {
		return (Profile) profilesList.getSelectedItem();
	}
	
	public ProfileVersion getSelectedVersion() {
		return (ProfileVersion) profilesVersionList.getSelectedItem();
	}
	
	public void setProfiles(List<Profile> profiles) {
		Profile selectedProfile = null;
		
		final String selectedProfileId = Services.getConfigurationService().get().getSelectedProfile();
		
		for (Profile p : profiles) {
			profilesModel.addElement(p);
			
			if ((selectedProfileId == null && selectedProfile == null) || p.getId().equals(selectedProfileId)) {
				selectedProfile = p;
			}
		}
		
		if (selectedProfile == null) {
			selectedProfile = profiles.get(0);
		}
		
		profilesList.setSelectedItem(selectedProfile);
	}
	
	protected void fireProfileSelected(Profile profile) {
		if (profile != null) {
			Services.getEventService().post(EventBusLine.FRAME, new ProfileSelectedEvent(profile));
			
			// Fill versions and get the preferred one
			ProfileVersion preferredVersion = null;
			
			profilesVersionModel.removeAllElements();
			for (ProfileVersion version : profile.getVersions()) {
				if (preferredVersion == null || version.isPreferred()) {
					preferredVersion = version;
				}
				profilesVersionModel.addElement(version);
			}
			
			profilesVersionList.setSelectedItem(preferredVersion);
			
			// Mise à jour des boutons
			versionDeleteButton.setEnabled(profile.getVersions().size() > 1);
		}
	}
	
	protected void fireProfileVersionSelected(ProfileVersion version) {
		if (version != null) {
			Services.getEventService().post(EventBusLine.FRAME, new ProfileVersionSelectedEvent(getSelectedProfile(), version));
			
			versionSetPreferredButton.setEnabled(!version.isPreferred());
			
		}
	}
}
