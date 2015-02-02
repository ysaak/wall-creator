package info.seravee.ui;

import info.seravee.data.ScalingAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {

    private final JFrame frame;

    private final DesktopPanel desktopPanel;

    private final JTabbedPane tabbedPane;

    public CreatorFrame() {
        frame = new JFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        desktopPanel = new DesktopPanel();

        tabbedPane = new JTabbedPane();

        buildFrame();
        frame.setSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);
    }

    private void buildFrame() {
        JComponent contentPane = (JComponent) frame.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(10, 10));
        
        contentPane.add(desktopPanel, BorderLayout.CENTER);
        contentPane.add(tabbedPane, BorderLayout.SOUTH);
    }

    public void setDesktopConfig(List<Rectangle> config) {
        int i = 0;
        for (Rectangle dc : config) {
            buildImageSelectPanel(dc, ++i);
        }
    }

    private void buildImageSelectPanel(Rectangle config, final int nbDesktop) {
        DesktopParameterPanel dpPanel = new DesktopParameterPanel();

        final JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dpPanel.build();
        panel.add(dpPanel.getDisplay(), BorderLayout.CENTER);

        tabbedPane.add("Desktop " + nbDesktop, panel);

        desktopPanel.addScreen(nbDesktop, config);

        dpPanel.setListener(new DesktopParameterPanel.DesktopParameterListener() {
            @Override
            public void imageSelected(File imageFile) {
                desktopPanel.setImage(nbDesktop, imageFile);
            }

            @Override
            public void scalingAlgorithmSelected(ScalingAlgorithm algorithm) {
                desktopPanel.setScalingAlgorithm(nbDesktop, algorithm);
            }

            @Override
            public void backgroundColorSelected(Color backgroundColor) {
                desktopPanel.setBackgroundColor(nbDesktop, backgroundColor);
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }
}
