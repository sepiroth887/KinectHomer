/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.ole.types.OleVerbs;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import excel.excel.Range;
import excel.excel.Sheets;
import excel.excel._Application;
import excel.excel._Worksheet;
import excel.excel.impl._ApplicationImpl;
import excel.excel.impl._WorksheetImpl;
import operations.OfficeFileOperationsHandler;
import operations.OfficePrintHandler;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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
public class ExcelIntegrationSample extends JFrame
{
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of excel document
     */
    private static final String DOCUMENT_PROGID = "Excel.Sheet";

    private OleContainer _container;

    public ExcelIntegrationSample()
    {
        super("JNIWrapper - Excel Integration");

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
            ((IUnknownImpl) iUnknown).setAutoDelete(false);
        }
    }

    /**
     * Returns instance of Excel application.
     *
     * @return instance of Excel application.
     */
    private _Application getApplication()
    {
        Automation automation = new Automation(_container.getOleObject());
        IDispatch dispApp = automation.getProperty("Application").getPdispVal();
        markAutoDelete(dispApp);

        _ApplicationImpl result = new _ApplicationImpl(dispApp);
        markAutoDelete(result);

        dispApp.release();

        return result;
    }

    /**
     * Modify embedded document using generated stubs.
     */
    public void modifyDocument()
    {
        System.out.println("Integration: modifying document...");

        _Application application = getApplication();

        try
        {
            // get 1st worksheet
            final Sheets worksheets = application.getWorksheets();
            markAutoDelete(worksheets);

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

    /**
     * Modify 1st worksheet using generated stubs.
     *
     * @param worksheets worksheets of Excel document.
     */
    private void modifyFirstWorksheet(final Sheets worksheets)
    {
        IDispatch dispatch = worksheets.getItem(new Variant(1));
        markAutoDelete(dispatch);

        try
        {
            _Worksheet worksheet = new _WorksheetImpl(dispatch);
            markAutoDelete(worksheet);

            try
            {
                // set values in cells of worksheet
                setValue(worksheet, "C1", "summa");
                setValue(worksheet, "A1", "Excel demo...");

                setValue(worksheet, "A2", "50");
                setValue(worksheet, "B2", "20");
                setValue(worksheet, "A3", "30");
                setValue(worksheet, "B3", "40");

                // set formulas for cells in worksheet
                setFormula(worksheet, "C2", "=A2+B2");
                setFormula(worksheet, "C3", "=A3+B3");

                // using optional parameters in dispatch call
//                saveAs(worksheet);

            }
            catch (Exception e)
            {
                e.printStackTrace();
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

    private void saveAs(_Worksheet worksheet)
    {
        Variant variant = Variant.createUnspecifiedParameter();

        String outputPath = "e:\\newfile.xls";

        try
        {
            worksheet._SaveAs(new BStr(outputPath),
                    variant,
                    variant,
                    variant,
                    variant,
                    variant,
                    variant,
                    variant,
                    variant,
                    new Int32(0)
                    );

            System.out.println("saved");
        }
        catch (ComException e)
        {
            System.out.println("Can't save file " + outputPath);
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
        markAutoDelete(range);

        try
        {
            Automation automation = new Automation(range);
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

    /**
     * Set formula for cell in worksheet.
     *
     * @param worksheet modified worksheet
     * @param cell coordinates of cell (f.e. "A3")
     * @param value formula (f.e. "=A1+A2")
     */
    private void setFormula(_Worksheet worksheet, String cell, String value)
    {
        Range range = worksheet.getRange(new Variant(cell), new Variant(cell));
        markAutoDelete(range);

        try
        {
            Automation automation = new Automation(range);
            try
            {
                automation.setProperty("Formula", value);
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
        final ExcelIntegrationSample app = new ExcelIntegrationSample();
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