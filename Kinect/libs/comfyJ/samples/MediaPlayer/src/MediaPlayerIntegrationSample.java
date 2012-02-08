/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Int16;
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.IEnumConnections;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import wmp.mediaplayer._MediaPlayerEvents;
import wmp.mediaplayer.impl.IMediaPlayerImpl;
import wmp.mediaplayer.server._MediaPlayerEventsServer;
import wmp.stdole.OLE_XPOS_PIXELS;
import wmp.stdole.OLE_YPOS_PIXELS;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


/**
 * This sample demonstrates the technique of embedding Windows Media Player application into a java application
 * using OleContainer and generated java stubs for Windows Media Player application.
 * <p/>
 *
 * This sample requires generated stubs for COM type library:
 * Description: Windows Media Player
 * ProgID:      MediaPlayer.MediaPlayer
 * GUID:        {22D6F304-B0F6-11D0-94AB-0080C74C7E95}
 * In the package: wmp
 *
 * You can generate stubs using the Code Generator application.
 *
 * @author Alexei Orischenko
 */
public class MediaPlayerIntegrationSample extends JFrame
{
    private static final String WINDOW_TITLE = "JNIWrapper - Media Player Integration";

    private OleContainer _container = new OleContainer();
    private IDispatchImpl _handler;

    public MediaPlayerIntegrationSample()
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
                setupListener();
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
        _container.createObject("MediaPlayer.MediaPlayer");
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
            OleMessageLoop.invokeMethod(this, "openFile", new Object[]{filename});
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
        IMediaPlayerImpl mediaPlayer = new IMediaPlayerImpl(_container.getOleObject());

        mediaPlayer.setPlayCount(new Int32(0)); // 0 == play continuously
        mediaPlayer.setFileName(new BStr(path));

        double duration = mediaPlayer.getDuration().getValue();
        logMessage("Duration of clip \"" + path + "\": " + Math.round(duration) + " seconds");
    }

    private void setupListener()
    {
        IClassFactoryServer server = new IClassFactoryServer(MediaPlayerListener.class);
        server.registerInterface(IDispatch.class, new IDispatchVTBL(server));
        server.registerInterface(_MediaPlayerEvents.class, new IDispatchVTBL(server));
        server.setDefaultInterface(IDispatch.class);

        IClassFactory classFactory = server.createIClassFactory();
        _handler = new IDispatchImpl();
        classFactory.createInstance(null, _handler.getIID(), _handler);

        IOleObjectImpl oleObject = _container.getOleObject();
        IConnectionPointContainer connectionPointContainer = new IConnectionPointContainerImpl(oleObject);
        IConnectionPoint connectionPoint =
                connectionPointContainer.findConnectionPoint(new IID(_MediaPlayerEvents.INTERFACE_IDENTIFIER));

        connectionPoint.advise(_handler);
    }

    public static class MediaPlayerListener extends _MediaPlayerEventsServer
    {
        public MediaPlayerListener(CoClassMetaInfo coClassMetaInfo)
        {
            super(coClassMetaInfo);
        }

        public void playStateChange(Int32 int32, Int32 int33)
        {
            logMessage("New state of Media Player component is " + int33.getValue());
        }

        public void click(Int16 int16, Int16 int17, OLE_XPOS_PIXELS ole_xpos_pixels, OLE_YPOS_PIXELS ole_ypos_pixels)
        {
            logMessage("Clicked on Media Player");
        }
    }

    public static void main(String[] args)
    {
        MediaPlayerIntegrationSample sample = new MediaPlayerIntegrationSample();
        sample.setVisible(true);
    }

//-------------------------------------------------------------------------------------------------
// Creation of Swing GUI
//-------------------------------------------------------------------------------------------------

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

        openItem.addActionListener(new OpenFileActionListener());

        exitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

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

    private static void logMessage(String msg)
    {
        System.out.println(msg);
    }

    private class OpenFileActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileFilter(new MediaPlayerFileFilter());

            if (chooser.showOpenDialog(MediaPlayerIntegrationSample.this) == JFileChooser.APPROVE_OPTION)
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
        private static final String[] MOVIE_FILES_EXTENSIONS = new String[]{"mpeg", "mpg", "avi", "wmv"};
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