package info.seravee.ui;

import info.seravee.data.ScalingAlgorithm;
import info.seravee.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by ysaak on 27/01/15.
 */
class DesktopParameterPanel {

    private final JPanel mainPanel;

    private final JTextField filenameField;
    private final JButton chooseFileButton;
    private final JComboBox scalingAlgoField;

    private DesktopParameterListener listener = null;

    public DesktopParameterPanel() {
        mainPanel = new JPanel();

        filenameField = new JTextField(20);
        filenameField.setEditable(false);

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
                    //This is where a real application would open the file.
                    System.out.println("Opening: " + file.getName() + ".");

                    filenameField.setText(file.getAbsolutePath());

                    if (listener != null) {
                        listener.imageSelected(file);
                    }
                }
                else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });

        scalingAlgoField = new JComboBox(ScalingAlgorithm.values());
        scalingAlgoField.setSelectedIndex(0);
        scalingAlgoField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScalingAlgorithm algo = (ScalingAlgorithm) scalingAlgoField.getSelectedItem();
                System.out.println(algo);

                if (listener != null) {
                    listener.scalingAlgorithmSelected(algo);
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
    }

    public JComponent getDisplay() {
        return mainPanel;
    }

    public void setListener(DesktopParameterListener listener) {
        this.listener = listener;
    }

    private class ImageFilter extends FileFilter {
        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = FileUtils.getExtension(f);
            return extension != null && FileUtils.IMAGE_EXTENSIONS.contains(extension.toLowerCase());
        }

        @Override
        public String getDescription() {
            return "Image files";
        }
    }

    public interface DesktopParameterListener {
        public void imageSelected(File imageFile);
        public void scalingAlgorithmSelected(ScalingAlgorithm algorithm);
    }
}
