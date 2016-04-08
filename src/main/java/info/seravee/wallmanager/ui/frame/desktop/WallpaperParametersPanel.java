package info.seravee.wallmanager.ui.frame.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import info.seravee.DefaultConfiguration;
import info.seravee.business.files.ImageFilter;
import info.seravee.data.ScalingAlgorithm;
import info.seravee.utils.SwingUtils;
import info.seravee.wallcreator.ui.GuiConstants;
import info.seravee.wallcreator.ui.components.ComponentFactory;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallcreator.ui.components.SolarizedColor;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;

public class WallpaperParametersPanel {
	
    private final JPanel mainPanel;
    
    private final JLabel imageLabel;
    private final JTextComponent imageField;
    private final JButton chooseFileButton;
    private final JButton clearButton;
    
    private final JLabel sizeLabel;
    private final JComboBox<ScalingAlgorithm> scalingAlgoField;
    
    private final JLabel colorLabel;
    private final JLabel colorDisplayLabel;
    private final JButton colorChooserButton;
    
    private WallpaperParameters parameters = null;
    
    private final Set<ScreenListener> screenListeners;

    public WallpaperParametersPanel() {
    	screenListeners = new HashSet<>();
    	
        mainPanel = new JPanel();

        imageLabel = new JLabel("Image:", SwingConstants.TRAILING);
        sizeLabel = new JLabel("Size:", SwingConstants.TRAILING);
        colorLabel = new JLabel("Color:", SwingConstants.TRAILING);
        
        imageField = ComponentFactory.createEditorPane();
        imageField.setText("rerzerzerzerzer");
        imageField.setEditable(false);
        imageField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    chooseFileButton.doClick();
                }
            }
        });

        chooseFileButton = new JButton("Select image");
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new ImageFilter());
                fc.setAcceptAllFileFilterUsed(false);

                int returnVal = fc.showOpenDialog(mainPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    
                    parameters.setImage(file.getAbsolutePath());
                    fireParameterChanged();
                }
            }
        });
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parameters.setImage(null);
				fireParameterChanged();
			}
		});

        scalingAlgoField = new JComboBox<>(ScalingAlgorithm.values());
        scalingAlgoField.setSelectedIndex(ScalingAlgorithm.indexOf(DefaultConfiguration.SCALING_ALGORITHM));
        scalingAlgoField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ScalingAlgorithm algo = (ScalingAlgorithm) scalingAlgoField.getSelectedItem();
                parameters.setScalingAlgorithm(algo);
                fireParameterChanged();
            }
        });

        
        colorDisplayLabel = new JLabel("      ");
        colorDisplayLabel.setOpaque(true);
        colorDisplayLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        colorDisplayLabel.setBackground(DefaultConfiguration.BACKGROUND_COLOR);
        colorDisplayLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooserButton.doClick();
            }
        });
        
        colorChooserButton = new JButton("Select color");
        colorChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(null, "Choose a Color", colorDisplayLabel.getBackground());
                
                if (c != null) {
                	parameters.setBackgroundColor(c);
                	fireParameterChanged();
                }
            }
        });
    }

    public void build() {
        mainPanel.setBorder(GuiConstants.BASE_EMPTY_BORDER);
        mainPanel.setOpaque(false);
        
        final GBCHelper gbc = new GBCHelper(mainPanel);
        
        SwingUtils.setSMPSizes(imageField, new Dimension(300, 75));
        imageField.setBorder(BorderFactory.createLineBorder(SolarizedColor.BASE1));
        
        // Image section
        gbc.addAnchoredComponent(imageLabel, 0, 0, GridBagConstraints.LINE_END);
        gbc.addSpanningFilledComponent(imageField, 1, 0, 5, 2, GridBagConstraints.BOTH);
        gbc.addComponent(clearButton, 4, 2);
        gbc.addComponent(chooseFileButton, 5, 2);
        
        
        // Size mode
        gbc.addAnchoredComponent(sizeLabel, 0, 3, GridBagConstraints.LINE_END);
        gbc.addSpanningFilledComponent(scalingAlgoField, 1, 3, 5, 1, GridBagConstraints.HORIZONTAL);

        
        // Background color
        gbc.addAnchoredComponent(colorLabel, 0, 4, GridBagConstraints.LINE_END);
        gbc.addComponent(colorDisplayLabel, 1, 4);
        gbc.addComponent(colorChooserButton, 2, 4);
    }

    public JComponent getDisplay() {
        return mainPanel;
    }
    
    public void setCurrentScreen(WallpaperParameters wallpaperParameter) {
    	
		this.parameters = wallpaperParameter;
		
		if (this.parameters != null) {
			// Update fields
			updateFieldsFromBean();
		}
	}
    
    public void updateFieldsFromBean() {
    	if (parameters != null) {
    		imageField.setText(parameters.getImage() != null ? parameters.getImage() : "");
    		scalingAlgoField.setSelectedItem(parameters.getScalingAlgorithm());
    		colorDisplayLabel.setBackground(parameters.getBackgroundColor());
    	}
    	else {
    		imageField.setText("");
    		scalingAlgoField.setSelectedItem(null);
    		colorDisplayLabel.setBackground(new Color(0,0,0,0));
    	}
    }

	protected void fireParameterChanged() {
		synchronized (screenListeners) {
			for (ScreenListener l : screenListeners) {
				l.screenParametersUpdated(parameters);
			}
		}
		updateFieldsFromBean();
	}
	
	public void addScreenListener(ScreenListener l) {
		synchronized (screenListeners) {
			screenListeners.add(l);
		}
	}
	
	public void removeScreenListener(ScreenListener l) {
		synchronized (screenListeners) {
			screenListeners.remove(l);
		}
	}
}

