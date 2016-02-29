package info.seravee.ui.creator;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import info.seravee.DefaultConfiguration;
import info.seravee.business.files.ImageFilter;
import info.seravee.data.ScalingAlgorithm;

/**
 * Created by ysaak on 27/01/15.
 */
class DesktopParameterPanel {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final JPanel mainPanel;

    private final JTextField filenameField;
    private final JButton chooseFileButton;
    private final JComboBox<ScalingAlgorithm> scalingAlgoField;
    
    private final JLabel colorDisplayLabel;
    private final JButton colorChooserButton;

    public DesktopParameterPanel() {
        mainPanel = new JPanel();

        filenameField = new JTextField(20);
        filenameField.setEditable(false);
        filenameField.addMouseListener(new MouseAdapter() {
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
                    setWallpaperFile(file);
                }
            }
        });

        scalingAlgoField = new JComboBox<ScalingAlgorithm>(ScalingAlgorithm.values());
        scalingAlgoField.setSelectedIndex(ScalingAlgorithm.indexOf(DefaultConfiguration.SCALING_ALGORITHM));
        scalingAlgoField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScalingAlgorithm algo = (ScalingAlgorithm) scalingAlgoField.getSelectedItem();
               	pcs.firePropertyChange("scalingAlgorithm", null, algo);
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
                    colorDisplayLabel.setBackground(c);
                    pcs.firePropertyChange("bgColor", null, c);
                }
            }
        });
    }

    public void build() {
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;

        c.gridx = 0;
        mainPanel.add(new JLabel("Image:", SwingConstants.TRAILING), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(filenameField, c);

        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        mainPanel.add(chooseFileButton, c);

        c.gridy = 1;

        c.gridx = 0;
        mainPanel.add(new JLabel("Size:", SwingConstants.TRAILING), c);

        c.gridx = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(scalingAlgoField, c);
        
        
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridx = 0;
        mainPanel.add(new JLabel("Color:", SwingConstants.TRAILING), c);
        
        c.gridx = 1;
        mainPanel.add(colorDisplayLabel, c);
        
        c.gridx = 2;
        mainPanel.add(colorChooserButton, c);
    }

    public JComponent getDisplay() {
        return mainPanel;
    }
    
    public void setWallpaperFile(File wallpaper) {
    	File oldValue = (filenameField.getText().length() == 0) ? null : new File(filenameField.getText());
    	filenameField.setText(wallpaper.getAbsolutePath());
    	this.pcs.firePropertyChange("file", oldValue, wallpaper);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
