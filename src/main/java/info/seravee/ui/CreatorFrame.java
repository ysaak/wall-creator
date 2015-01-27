package info.seravee.ui;

import info.seravee.data.ScalingAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {

    private final JFrame frame;

    private final ImageDisplayer imageDisplayer;
    private final DesktopParameterPanel desktop1ParameterPanel;

    public CreatorFrame() {
        frame = new JFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imageDisplayer = new ImageDisplayer();
        desktop1ParameterPanel = new DesktopParameterPanel();

        desktop1ParameterPanel.setListener(new DesktopParameterPanel.DesktopParameterListener() {
            @Override
            public void imageSelected(File imageFile) {
                imageDisplayer.setImage(imageFile);
            }

            @Override
            public void scalingAlgorithmSelected(ScalingAlgorithm algorithm) {
                imageDisplayer.setScalingAlgorithm(algorithm);
            }
        });

        buildFrame();
        frame.setSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);
    }

    private void buildFrame() {
        JComponent contentPane = (JComponent) frame.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(10, 10));

        contentPane.add(imageDisplayer, BorderLayout.CENTER);

        final JPanel parametersPanel = new JPanel(new BorderLayout(10, 10));
        parametersPanel.add(buildImageSelectPanel(desktop1ParameterPanel, 1), BorderLayout.WEST);

        contentPane.add(parametersPanel, BorderLayout.SOUTH);
    }

    private JPanel buildImageSelectPanel(DesktopParameterPanel dpPanel, int nbDesktop) {
        final JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Desktop " + nbDesktop));
        dpPanel.build();
        panel.add(dpPanel.getDisplay(), BorderLayout.CENTER);
        return panel;
    }

    public void show() {
        frame.setVisible(true);
    }
}
