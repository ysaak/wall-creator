package info.seravee.wallcreator.ui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

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
import info.seravee.wallcreator.beans.Screen;
import info.seravee.wallcreator.ui.GuiConstants;
import info.seravee.wallcreator.ui.components.ComponentFactory;
import info.seravee.wallcreator.ui.components.GBCHelper;
import info.seravee.wallcreator.ui.components.SolarizedColor;

/**
 * Created by ysaak on 27/01/15.
 */
public class WallpaperParametersPanel implements ScreenListener {
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
    
    
    private final PropertyChangeListener screenChangeListener;
    private Screen currentScreen = null;

    public WallpaperParametersPanel() {
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
                    
                    currentScreen.setImageFile(file);
                }
            }
        });
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentScreen.setImageFile(null);
			}
		});

        scalingAlgoField = new JComboBox<>(ScalingAlgorithm.values());
        scalingAlgoField.setSelectedIndex(ScalingAlgorithm.indexOf(DefaultConfiguration.SCALING_ALGORITHM));
        scalingAlgoField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ScalingAlgorithm algo = (ScalingAlgorithm) scalingAlgoField.getSelectedItem();
                currentScreen.setScalingAlgorithm(algo);
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
                	currentScreen.setBackgroundColor(c);
                }
            }
        });
        
        screenChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateFieldsFromBean();
			}
		};
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
    
    public void setCurrentScreen(Screen currentScreen) {
    	
    	if (this.currentScreen != null) {
    		this.currentScreen.removePropertyChangeListener(screenChangeListener);
    	}
    	
		this.currentScreen = currentScreen;
		
		if (this.currentScreen != null) {
			// Update fields
			updateFieldsFromBean();
		
			this.currentScreen.addPropertyChangeListener(screenChangeListener);
		}
	}
    
    protected void updateFieldsFromBean() {
    	if (currentScreen != null) {
    		imageField.setText(currentScreen.getImageFile() != null ? currentScreen.getImageFile().getAbsolutePath() : "");
    		scalingAlgoField.setSelectedItem(currentScreen.getScalingAlgorithm());
    		colorDisplayLabel.setBackground(currentScreen.getBackgroundColor());
    	}
    	else {
    		imageField.setText("");
    		scalingAlgoField.setSelectedItem(null);
    		colorDisplayLabel.setBackground(new Color(0,0,0,0));
    	}
    }

	@Override
	public void screenSelected(Screen screen) {
		setCurrentScreen(screen);
	}
}
