/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.IOleObject;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.impl.IOleObjectImpl;
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

import pdf.acropdflib.AcroPDF;
import pdf.acropdflib.IAcroAXDocShim;
import pdf.acropdflib.impl.IAcroAXDocShimImpl;

/**
 * This sample demonstrates the technique of embedding Acrobat Reader application into a java application
 * using OleContainer and generated java stubs for Acrobat Reader application.
 *
 * This sample requires generated stubs for COM type library:
 * Description: Adobe Acrobat 7.0 Type Library
 * ProgID:      AcroPDF.PDF
 * GUID:        {05BFD3F1-6319-4F30-B752-C7A22889BCC4}
 * In the package: pdf
 *
 * You can generate stubs using the Code Generator application.
 *
 * @author Alexei Orischenko
 */
public class AcrobatReader7IntegrationSample extends JFrame
{
    private static final String WINDOW_TITLE = "JNIWrapper - Acrobat Reader Integration";

    private OleContainer _container = new OleContainer();

    public AcrobatReader7IntegrationSample()
    {
        super(WINDOW_TITLE);

        init();
    }

    private void init()
    {
        setWindowsLookFeel();

        getContentPane().add(_container, BorderLayout.CENTER);

        doCreateOleObject();

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

    private void doCreateOleObject()
    {
        try
        {
            OleMessageLoop.invokeMethod(this, "createOleObject");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Creates embedded object.
     */
    public void createOleObject()
    {
        IAcroAXDocShim areader = AcroPDF.create(ClsCtx.INPROC_SERVER);
        IOleObject oleObject = new IOleObjectImpl(areader);
        _container.insertObject(oleObject);
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
     * @param filePath absolute path of loaded file.
     */
    public void openFile(String filePath)
    {
        File file = new File(filePath);

        if (!file.exists())
        {
            throw new RuntimeException("Couldn't find file with document: " + filePath);
        }

        IAcroAXDocShim aReader = new IAcroAXDocShimImpl(_container.getOleObject());

        aReader.setSrc(new BStr(filePath));
        aReader.loadFile(new BStr(filePath));

        System.out.println("loaded: " + aReader.getSrc());
    }

    public static void main(String[] args)
    {
        // initialize OLE
        OleFunctions.oleInitialize();

        AcrobatReader7IntegrationSample sample = new AcrobatReader7IntegrationSample();
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

        openItem.addActionListener(new AcrobatReader7IntegrationSample.OpenFileActionListener());

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
            chooser.setFileFilter(new AcrobatReader7IntegrationSample.AcrobatFileFilter());

            if (chooser.showOpenDialog(AcrobatReader7IntegrationSample.this) == JFileChooser.APPROVE_OPTION)
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