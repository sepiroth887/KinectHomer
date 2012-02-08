/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.jniwrapper.win32.gdi.PrintParameters;
import com.jniwrapper.Int32;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import excel.excel.*;
import excel.excel.impl._ApplicationImpl;
import excel.excel.impl._WorksheetImpl;
import excel.excel.impl._WorkbookImpl;
import operations.OfficeFileOperationsHandler;
import operations.OfficePrintHandler;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;


/**
 * This sample demonstrates the technique of embedding Excel application into a java application
 * using OleContainer and generated java stubs for Excel application.
 *
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Excel 11.0 Object Library
 * ProgID:      Excel.Sheet
 * GUID:        {00020813-0000-0000-C000-000000000046}
 * In the package: excel
 *
 * You can generate stubs using the Code Generator application.
 *
 * @author Alexei Orischenko
 */
public class ExcelPrintSample extends JFrame
{
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of excel document
     */
    private static final String DOCUMENT_PROGID = "Excel.Sheet";

    private OleContainer _container;
    private OfficePrintHandler _printHandler = new OfficePrintHandler();

    public ExcelPrintSample()
    {
        super("Excel Print Demo");

        _container = new OleContainer();
        _container.createObject(DOCUMENT_PROGID);

        getContentPane().add(_container, BorderLayout.CENTER);

        // Enable open, save of excel document (Excel toolbar)
        _container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_EXCEL));

        // Enable printing, print preview (Excel toolbar)
        _container.setPrintDocumentHandler(_printHandler);
    }

    /**
     * Returns instance of Excel application.
     *
     * @return instance of Excel application.
     */
    private _ApplicationImpl getApplication()
    {
        IUnknownImpl iUnknown = (IUnknownImpl)ComFunctions.getActiveObject(Application.CLASS_ID);
        iUnknown.setAutoDelete(false);

        try
        {
            _ApplicationImpl application = new _ApplicationImpl(iUnknown);
            application.setAutoDelete(false);

            return application;
        }
        finally
        {
            iUnknown.release();
        }
    }

    /**
     * Modify embedded document using generated stubs.
     */
    public void modifyDocument()
    {
        _ApplicationImpl application = getApplication();

        try
        {
            // get 1st worksheet
            final Sheets worksheets = application.getWorksheets();
            ((IUnknownImpl)worksheets).setAutoDelete(false);

            try
            {
                modifyFirstWorksheet(worksheets);
            }
            finally
            {
                worksheets.release();
            }
        }
        finally
        {
            application.release();
        }
    }

    private void printDocument()
    {
        PrintParameters printParameters = new PrintParameters();

        try
        {
            OleMessageLoop.invokeMethod(_printHandler, "print");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void printDocumentWithoutDialog()
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                _Workbook workbook = new _WorkbookImpl(_container.getOleObject());
                int lastSheet = (int)workbook.getWorksheets().getCount().getValue();
                int copies = 1;
                workbook.printOut(new Variant(1),
                        new Variant(lastSheet + 1),
                        new Variant(copies),
                        new Variant(false),
                        Variant.createUnspecifiedParameter(),
                        Variant.createUnspecifiedParameter(),
                        Variant.createUnspecifiedParameter(),
                        Variant.createUnspecifiedParameter(),
                        new Int32(0));
            }
        };
        try
        {
            OleMessageLoop.getInstance().doInvokeAndWait(runnable);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Modify 1st worksheet using generated stubs.
     *
     * @param worksheets worksheets of Excel document.
     */
    private void modifyFirstWorksheet(final Sheets worksheets)
    {
        IDispatchImpl dispatch = (IDispatchImpl)worksheets.getItem(new Variant(1));
        dispatch.setAutoDelete(false);

        try
        {
            _WorksheetImpl worksheet = new _WorksheetImpl(dispatch);
            worksheet.setAutoDelete(false);

            try
            {
                setValue(worksheet, "A1", "Document to print");
                setValue(worksheet, "A101", "Next page");
            }
            finally
            {
                worksheet.release();
            }
        }
        finally
        {
            dispatch.release();
        }
    }

    /**
     * Set value in cell of worksheet.
     *
     * @param worksheet modified worksheet
     * @param cell coordinates of modified cell (f.e. "A1")
     * @param value new value in cell
     */
    private void setValue(_Worksheet worksheet, String cell, String value)
    {
        Range range = worksheet.getRange(new Variant(cell), new Variant(cell));
        ((IUnknownImpl)range).setAutoDelete(false);

        try
        {
            Automation automation = new Automation((IDispatchImpl)range);
            try
            {
                automation.setProperty("Value", value);
            }
            finally
            {
                automation.release();
            }
        }
        finally
        {
            range.release();
        }
    }

    private static void createGUI()
    {
        final ExcelPrintSample app = new ExcelPrintSample();
        app.setSize(WINDOW_SIZE);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try
        {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        }
        catch (UnsupportedLookAndFeelException e)
        {
        }

        app.createMenu();

        app.addWindowListener(new WindowAdapter()
        {
            public void windowOpened(WindowEvent e)
            {
                // show excel
                app._container.doVerb(OleVerbs.SHOW);

                // work with excel using generated sources
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
                app.exit();
            }
        });

        app.setVisible(true);
    }

    private void exit()
    {
        _container.destroyObject();
    }

    public static void main(String[] args)
    {
        // initialize OLE
        OleFunctions.oleInitialize();

        createGUI();
    }

    private void createMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        createFileMenu(menuBar);

        setJMenuBar(menuBar);
    }

    private void createFileMenu(JMenuBar menuBar)
    {
        JMenu menu = new JMenu("File");

        JMenuItem printItem = new JMenuItem("Print");
        printItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Invoke long-term action in separate thread
                Runnable runnable = new Runnable() {
                    public void run()
                    {
                        printDocument();
                    }
                };

                new Thread(runnable).start();
            }
        });

        menu.add(printItem);

        printItem = new JMenuItem("Print without dialog");
        printItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                printDocumentWithoutDialog();
            }
        });

        menu.add(printItem);

        menu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                exit();
                System.exit(0);
            }
        });

        menu.add(exitItem);
        menuBar.add(menu);
    }
}