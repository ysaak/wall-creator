package info.seravee;

import info.seravee.platform.Platform;
import info.seravee.platform.Platforms;
import info.seravee.ui.CreatorFrame;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    CreatorFrame frame;

    public App() {
        frame = new CreatorFrame();

        final Platform p = Platforms.get();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setDesktopConfig(p.getDesktopConfiguration());
                frame.show();
            }
        });
    }

    public static void main( String[] args )
    {
        new App();
    }
}
