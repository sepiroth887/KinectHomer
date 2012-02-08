/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;


/**
 * This sample demonstrates the technique of embedding Acrobat Reader application into a java application
 * using OleContainer and Automation.
 *
 * @author Alexei Orischenko
 */
public class AcrobatReader7AutomationSample extends JFrame
{
    private static final String WINDOW_TITLE = "JNIWrapper - Acrobat Reader Automation";

    private static final String ACROBAT_READER_PROGID = "AcroPDF.PDF";

    private OleContainer _container = new OleContainer();

    public AcrobatReader7AutomationSample()
    {
        super(WINDOW_TITLE);

        init();
    }

    private void init()
    {
        setWindowsLookFeel();

        getContentPane().add(_container, BorderLayout.CENTER);

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
        _container.createObject(ACROBAT_READER_PROGID);
    }

    /**
     * Activates embedded object.
     */
    private void showOleObject()
    {
        _container.doVerb(OleVerbs.SHOW);
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
     * @param filePath absolute path of loaded file.
     */
    public void openFile(String filePath)
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new RuntimeException("Couldn't find file: " + filePath);
        }
        IDispatchImpl acrobatReader = new IDispatchImpl(_container.getOleObject());
        Automation automation = new Automation(acrobatReader);

        // load document
        automation.setProperty("src", filePath);
        automation.invoke("LoadFile", new Object[] { filePath });
    }

    public static void main(String[] args)
    {
        // initialize OLE
        OleFunctions.oleInitialize();

        AcrobatReader7AutomationSample sample = new AcrobatReader7AutomationSample();
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

    private class OpenFileActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser chooser = new JFileChooser(".");
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileFilter(new AcrobatFileFilter());

            if (chooser.showOpenDialog(AcrobatReader7AutomationSample.this) == JFileChooser.APPROVE_OPTION)
            {
                File file = chooser.getSelectedFile();
                if (file != null)
                {
                    doOpen(file.getAbsolutePath());
                }
            }
        }
    }

    private static class AcrobatFileFilter extends FileFilter
    {
        private static final String FLASH_FILE_EXTENSION = "pdf";
        private static final String DESCRIPTION = "PDF files (*.pdf)";

        private static final String EXTENSION_SEPARATOR = ".";

        public boolean accept(File pathname)
        {
            if (pathname.isFile())
            {
                String name = pathname.getName();
                int pos = name.lastIndexOf(EXTENSION_SEPARATOR);
                String extension = pos != -1 ? name.substring(pos + 1) : "";

                return FLASH_FILE_EXTENSION.equals(extension);
            }

            return true;
        }

        public String getDescription()
        {
            return DESCRIPTION;
        }
    }
}