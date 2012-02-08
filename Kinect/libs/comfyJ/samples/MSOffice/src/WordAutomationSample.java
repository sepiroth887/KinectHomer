/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.IAdviseSink;
import com.jniwrapper.win32.ole.types.OleVerbs;
import operations.OfficeFileOperationsHandler;
import operations.OfficePrintHandler;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * This sample demonstrates the technique of embedding Word application into java application
 * using OleContainer and Automation.
 * 
 * @author Alexei Orischenko
 */
public class WordAutomationSample extends JFrame
{
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of word document
     */
    private static final String DOCUMENT_PROGID = "Word.Document";

    private OleContainer _container;

    public WordAutomationSample()
    {
        super("JNIWrapper - Word Automation");

        _container = new OleContainer();
        _container.createObject(DOCUMENT_PROGID);

        getContentPane().add(_container, BorderLayout.CENTER);

        // Enable open / save operations
        _container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_WORD));

        // Enable print / print preview
        _container.setPrintDocumentHandler(new OfficePrintHandler());
    }

    public void modifyDocument()
    {
        // get word document from container object
        IDispatchImpl document = new IDispatchImpl(_container.getOleObject());
        document.setAutoDelete(false);

        try
        {
            Automation automation = new Automation(document);

            // get word application
            IDispatchImpl application = (IDispatchImpl) automation.getProperty("Application").getPdispVal();
            application.setAutoDelete(false);

            try
            {
                Automation appAutomation = new Automation(application);

                // print version of word application
                String version = appAutomation.getProperty("Version").getBstrVal().getValue();
                System.out.println("version = " + version);
            }
            finally
            {
                application.release();
            }
        }
        finally
        {
            document.release();
        }
    }

    private static void createGUI()
    {
        final WordAutomationSample app = new WordAutomationSample();
        app.setSize(WINDOW_SIZE);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        app.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                // show word
                app._container.doVerb(OleVerbs.SHOW);

                // work with word through automation
                try
                {
                    OleMessageLoop.invokeMethod(app, "modifyDocument", new Object[] {});
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            public void windowClosing(WindowEvent e)
            {
                // close document on exit
                app._container.destroyObject();
            }
        });

        app.setVisible(true);
    }

    public static void main(String[] args)
    {
        // initialize OLE
        OleFunctions.oleInitialize();

        createGUI();
    }
}