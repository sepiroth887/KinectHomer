/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.ole.OleFunctions;
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
 * This sample demonstrates the technique of embedding Excel application into a java application
 * using OleContainer and Automation.
 *
 * @author Alexei Orischenko
 */
public class ExcelAutomationSample extends JFrame
{
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of excel document
     */
    private static final String DOCUMENT_PROGID = "Excel.Sheet";

    private OleContainer _container;

    public ExcelAutomationSample()
    {
        super("JNIWrapper - Excel Automation");

        _container = new OleContainer();
        _container.createObject(DOCUMENT_PROGID);

        getContentPane().add(_container, BorderLayout.CENTER);

        // Enable open, save of excel document (Excel toolbar)
        _container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_EXCEL));

        // Enable printing, print preview (Excel toolbar)
        _container.setPrintDocumentHandler(new OfficePrintHandler());
    }

    private static void markAutoDelete(IUnknown iUnknown)
    {
        if (iUnknown instanceof IUnknownImpl)
        {
            ((IUnknownImpl)iUnknown).setAutoDelete(false);
        }
    }

    /**
     * Returns active object if active object is embedded else returns null.
     * <p/>
     * <p><b>Note.</b> This method returns null if there isn't excel active object or
     * excel active object belongs to another application.
     *
     * @return active object if active object is embedded.
     */
    private IDispatch getApplication()
    {
        Automation automation = new Automation(_container.getOleObject());
        IDispatch dispApp = automation.getProperty("Application").getPdispVal();

        return dispApp;
    }

    /**
     * Modify document using OLE Automation
     */
    public void modifyDocument()
    {
        IDispatch app = getApplication();

        try
        {
            Automation applicationAutomation = new Automation(app);

            try
            {
                // get worksheets
                IDispatch worksheets = applicationAutomation.getProperty("Worksheets").getPdispVal();
                markAutoDelete(worksheets);

                try
                {
                    Automation worksheetsAutomation = new Automation(worksheets);

                    try
                    {
//                        IDispatch worksheet = getFirstWorksheet(worksheetsAutomation);
//                        worksheet.release();
                    }
                    finally
                    {
                        worksheetsAutomation.release();
                    }
                }
                finally
                {
                    worksheets.release();
                }
            }
            finally
            {
                applicationAutomation.release();
            }
        }
        finally
        {
            app.release();
        }
    }

    private IDispatch getFirstWorksheet(Automation worksheetsAutomation)
    {
        IDispatch worksheet = worksheetsAutomation.getProperty("Item", 1).getPdispVal();
        markAutoDelete(worksheet);

        return worksheet;
    }

    private static void createGUI()
    {
        final ExcelAutomationSample app = new ExcelAutomationSample();
        app.setSize(WINDOW_SIZE);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        app.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                // show excel
                app._container.doVerb(OleVerbs.INPLACEACTIVATE);

                // work with excel through automation
                try
                {
                    OleMessageLoop.invokeMethod(app, "modifyDocument", new Object[]{});
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            public void windowClosing(WindowEvent e)
            {
                // close excel document on exit
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