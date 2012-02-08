/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * This sample demonstrates the technique of embedding Windows Media Player application into a java application
 * using OleContainer and Automation.
 *
 * @author Alexei Orischenko
 */
public class MediaPlayerAutomationSample extends JFrame
{
    private static final String WINDOW_TITLE = "JNIWrapper - Media Player Automation";

    private static final String MEDIA_PLAYER_PROGID = "MediaPlayer.MediaPlayer";

    private OleContainer _container = new OleContainer();

    public MediaPlayerAutomationSample()
    {
        super(WINDOW_TITLE);

        init();
    }

    private void init()
    {
        setWindowsLookFeel();

        getContentPane().add(_container);

        createOleObject();

        addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                showOleObject();
            }

            public void windowClosing(WindowEvent e)
            {
                destroyOleObject();
            }
        });

        createMenu();
        setFrameProperties();
    }

    /**
     * Creates embedded object.
     */
    private void createOleObject()
    {
        _container.createObject(MEDIA_PLAYER_PROGID);
    }

    /**
     * Activates embedded object.
     */
    private void showOleObject()
    {
        _container.doVerb(OleVerbs.INPLACEACTIVATE);
    }

    /**
     * Destroys embedded object.
     */
    private void destroyOleObject()
    {
        _container.destroyObject();
    }

    private void doOpen(String filename)
    {
        try
        {
            OleMessageLoop.invokeMethod(this, "openFile", new Object[] { filename });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads file into embedded object.
     *
     * @param path absolute path of loaded file.
     */
    public void openFile(String path)
    {
        IOleObjectImpl oleObject = _container.getOleObject();

        Automation automation = new Automation(oleObject);
        automation.setProperty("FileName", path);

        Variant vDuration = automation.getProperty("Duration");

        double duration = vDuration.getDblVal().getValue();
        System.out.println("Duration of clip \"" + path + "\": " + Math.round(duration) + " seconds");
    }

    public static void main(String[] args)
    {
        MediaPlayerAutomationSample sample = new MediaPlayerAutomationSample();
        sample.setVisible(true);
    }

//-------------------------------------------------------------------------------------------------
// Creation of Swing GUI
//-------------------------------------------------------------------------------------------------

    private void setWindowsLookFeel()
    {
        try
        {
            UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
        }
        catch (Exception e)
        {
        }
    }

    private void setFrameProperties()
    {
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createMenu()
    {
        // prevent hiding of popup menu under OleContaner component
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem exitItem = new JMenuItem("Exit");

        menu.add(openItem);
        menu.addSeparator();
        menu.add(exitItem);

        openItem.addActionListener(new MediaPlayerAutomationSample.OpenFileActionListener());

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                destroyOleObject();
                System.exit(0);
            }
        });

        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

    private class OpenFileActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileFilter(new MediaPlayerFileFilter());

            if (chooser.showOpenDialog(MediaPlayerAutomationSample.this) == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if (file != null)
                {
                    doOpen(file.getAbsolutePath());
                }
            }
        }
    }

    private static class MediaPlayerFileFilter extends FileFilter
    {
        private static final String[] MOVIE_FILES_EXTENSIONS = new String[] { "mpeg", "mpg", "avi", "wmv" };
        private static final String DESCRIPTION = "Movie files (*.mpeg, *.mpg, *.avi, *.wmv)";

        private static final String EXTENSION_SEPARATOR = ".";

        public boolean accept(File pathname)
        {
            if (pathname.isFile())
            {
                String name = pathname.getName();
                int pos = name.lastIndexOf(EXTENSION_SEPARATOR);
                String extension = pos != -1 ? name.substring(pos + 1) : "";

                for (int i = 0; i < MOVIE_FILES_EXTENSIONS.length; i++)
                {
                    String movieExtension = MOVIE_FILES_EXTENSIONS[i];

                    if (movieExtension.equals(extension))
                    {
                        return true;
                    }
                }

                return false;
            }

            return true;
        }

        public String getDescription()
        {
            return DESCRIPTION;
        }
    }
}