package info.seravee;

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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.show();
            }
        });
    }

    public static void main( String[] args )
    {
        new App();
    }
}
