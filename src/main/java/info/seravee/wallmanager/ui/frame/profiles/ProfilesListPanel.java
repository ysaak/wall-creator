package info.seravee.wallmanager.ui.frame.profiles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;

import info.seravee.wallcreator.ui.components.DropShadowBorder;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.ui.commons.I18N;

public final class ProfilesListPanel {
	private final JPanel panel;
	private final JPanel actionsPanel;
	private final JComponent overlayPanel;

	private final JLabel profileLabel;
	private final DefaultComboBoxModel<Profile> profilesModel;
	private final JComboBox<Profile> profilesList;

	private final JLabel versionLabel;
	private final DefaultComboBoxModel<ProfileVersion> profilesVersionModel;
	private final JComboBox<ProfileVersion> profilesVersionList;
	
	private final JButton profileCreateButton;
	private final JButton profileRenameButton;
	
	private final JButton versionAddButton;
	private final JButton versionRenameButton; 
	private final JButton versionSetPreferredButton;
	private final JButton versionDeleteButton;
	
	private ProfileListListener listener = null;
	
	
	private boolean actionsPanelVisible = false;
	private Timer slideTimer = null;
	
	private static final int SLIDE_INCREMENT = 10;
	private static final int SLIDE_EVENT_INTERVAL = 50;
	
	private final JButton tstButton = new JButton("open");

	public ProfilesListPanel() {
		this.panel = new JPanel();
		this.actionsPanel = new JPanel();
		this.overlayPanel = new JPanel();
		overlayPanel.setVisible(false);

		// Profiles list
		profileLabel = new JLabel(I18N.get("ProfilePanel.profile"));
		profileLabel.setForeground(Color.WHITE);
		profileLabel.setFont(profileLabel.getFont().deriveFont(Font.BOLD));
		
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
		
		profileCreateButton = new JButton(I18N.get("ProfilePanel.profile.create"));
		profileCreateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = (String) JOptionPane.showInputDialog(panel, "New name", "Rename profile", JOptionPane.QUESTION_MESSAGE, null, null, null);
				
				fireProfileCreate(name);
			}
		});
		
		profileRenameButton = new JButton(I18N.get("ProfilePanel.profile.rename"));
		profileRenameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final Profile p = getSelectedProfile();
				
				Object newName = (String) JOptionPane.showInputDialog(panel, "New name", "Rename profile", JOptionPane.QUESTION_MESSAGE, null, null, p.getName());
				
				if (newName != null) {
					p.setName((String) newName);
					
					fireProfileUpdated(p);
				}
			}
		});
		
		/* --------------------------------------------------------------------- */

		// Profile's versions
		versionLabel = new JLabel(I18N.get("ProfilePanel.version"));
		versionLabel.setForeground(Color.WHITE);
		versionLabel.setFont(profileLabel.getFont().deriveFont(Font.BOLD));
		
		profilesVersionModel = new DefaultComboBoxModel<>();
		profilesVersionList = new JComboBox<>(profilesVersionModel);
		profilesVersionList.setLightWeightPopupEnabled(false);
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
				
				if (listener != null)
					listener.markProfileVersionAsPrefered(p, pv);
				
				versionSetPreferredButton.setEnabled(false);
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
					
					fireProfileUpdated(getSelectedProfile());
					
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
					
					fireProfileUpdated(p);
					
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
					fireProfileUpdated(profile);
					
					profilesVersionModel.removeElement(version);
					fireProfileSelected(profile);
				}
			}
		});
		
		tstButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showActionsPanel();
			}
		});
	}
	
	private void showActionsPanel() {
		stopSlideTimer();
		
		actionsPanelVisible = !actionsPanelVisible;
		
		slideTimer = new Timer(SLIDE_EVENT_INTERVAL, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				slideActionPanel();
			}
		});
		
		slideTimer.start();
	}
	
	private void slideActionPanel() {
		
		Rectangle oldBounds = actionsPanel.getBounds();
		oldBounds.y += (actionsPanelVisible ? 1 : -1) * SLIDE_INCREMENT;
		actionsPanel.setBounds(oldBounds);
		
		Rectangle overlayBounds = overlayPanel.getBounds();
		overlayBounds.y += (actionsPanelVisible ? 1 : -1) * SLIDE_INCREMENT;
		overlayPanel.setBounds(overlayBounds);
		
		// Overlay opacity
		int rasY = overlayBounds.y + 8; // 0 - 50
		float opacity = (rasY - 50) / 100f;
		overlayPanel.setBackground(new Color(0,0,0, opacity));
		
		if (oldBounds.y == 42 || oldBounds.y == -8) {
			stopSlideTimer();
		}
		
		if (oldBounds.y == -8) {
			overlayPanel.setVisible(false);
		}
		else if (!overlayPanel.isVisible()) {
			overlayPanel.setVisible(true);
		}
	}
	
	private void stopSlideTimer() {
		if (slideTimer != null) {
			slideTimer.stop();
			slideTimer = null;
		}
	}

	public void buildPanel() {
		final JPanel listPanel = buildListPanel();
		buildActionsPanel();
		
		createBorderedPanel(panel, listPanel);
	}
	
	private JPanel buildListPanel() {
		// https://github.com/JTWalraven/macwidgets/tree/master/src/main/java/com/jtechdev/macwidgets/plaf
		
		final JPanel innerPanel = new JPanel();
		innerPanel.setBackground(Color.BLACK);
		
		GBCHelper gbc = new GBCHelper(innerPanel);
		gbc.addComponent(profileLabel, 0, 0);
		gbc.addComponent(profilesList, 1, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addComponent(versionLabel, 2, 0);
		gbc.addComponent(profilesVersionList, 3, 0, 1.0, 0.0, 1, 1, GBCHelper.DEFAULT_ANCHOR, GridBagConstraints.HORIZONTAL);
		gbc.addComponent(tstButton, 4, 0);

		return innerPanel;
	}
	
	private void buildActionsPanel() {
		overlayPanel.setBackground(new Color(0,0,0, 0.5f));
		overlayPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				showActionsPanel();
			}
		});
		
		actionsPanel.setBackground(Color.DARK_GRAY.darker().darker());
		
		GBCHelper gbc = new GBCHelper(actionsPanel);
		
		gbc.addComponent(buildProfileButtonPanel(), 0, 0, 2.0, 0.0, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
		gbc.addComponent(buildVersionButtonPanel(), 1, 0, 1.0, 0.0, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL);
	}
	
	private JPanel buildProfileButtonPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		buttonsPanel.setOpaque(false);
		
		buttonsPanel.add(profileCreateButton);
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

	private void createBorderedPanel(final JPanel container, final JPanel innerPanel) {
		container.setBackground(new Color(1f,1f,1f,0f));
		
		DropShadowBorder border = new DropShadowBorder(Color.BLACK, 8, .5f, 0, false, false, true, false);
		border.setFullsizeBottomBorder(true);
		container.setBorder(border);
		
		container.setLayout(new BorderLayout());
		container.add(innerPanel, BorderLayout.CENTER);
	}
	
	public JPanel getDisplay() {
		return panel;
	}
	
	public JPanel getActionsPanel() {
		return actionsPanel;
	}
	
	public JComponent getOverlayPanel() {
		return overlayPanel;
	}
	
	public Profile getSelectedProfile() {
		return (Profile) profilesList.getSelectedItem();
	}
	
	public ProfileVersion getSelectedVersion() {
		return (ProfileVersion) profilesVersionList.getSelectedItem();
	}

	public void addProfile(Profile newProfile) {
		profilesModel.addElement(newProfile);
		profilesList.setSelectedItem(newProfile);
	}
	
	public void profileUpdated(Profile profile) {
		profilesList.repaint();
		
		if (getSelectedProfile().equals(profile)) {
			profilesVersionList.repaint();
		}
	}
	
	
	public void removeProfile(Profile profile) {
		// FIXME
	}
	
	
	public void setProfiles(List<Profile> profiles) {
		Profile selectedProfile = null;
		
		for (Profile p : profiles) {
			profilesModel.addElement(p);
			
			if (selectedProfile == null || p.isSelected()) {
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
			
			if (listener != null)
				listener.profileSelected(profile);
			
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
			if (listener != null)
				listener.profileVersionSelected(getSelectedProfile(), version);
			
			versionSetPreferredButton.setEnabled(!version.isPreferred());
		}
	}
	
	protected void fireProfileCreate(String name) {
		if (listener != null)
			listener.profileCreate(name);
	}
	
	protected void fireProfileUpdated(Profile profile) {
		if (listener != null)
			listener.profileUpdated(profile);
	}

	public void setListener(ProfileListListener listener) {
		this.listener = listener;
	}
}
