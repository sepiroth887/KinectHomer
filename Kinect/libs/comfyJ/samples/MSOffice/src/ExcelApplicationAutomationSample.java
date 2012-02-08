/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;

import java.io.File;

/**
 * This sample opens existing Excel file, modifies cell A1,<br>
 * and saves changes to jniwrapper.xls in the directory of source file.
 *
 * @author Alexei Orischenko
 */
public class ExcelApplicationAutomationSample
{
    private static final String USAGE_STR = "Usage: java ExcelApplicationAutomationSample <file>, \n" +
                               "           <file> is name of excel file in working directory, or full path to excel file";

    private static final String NEW_VALUE = "Modified by JNIWrapper";
    private static final String OUTPUT_FILENAME = "jniwrapper.xls";

    public void doWork(File inputFile)
    {
        checkInputFile(inputFile);

        // Create instance of Excel application
        CLSID clsid = CLSID.createFromProgID("Excel.Application");
        IUnknown applicationUnknown = ComFunctions.coCreateInstance(clsid, null, ClsCtx.LOCAL_SERVER);
        IDispatch application = new IDispatchImpl(applicationUnknown);

        IDispatch workbooks = null;
        IDispatch workbook = null;
        IDispatch worksheets = null;
        IDispatch worksheet = null;
        IDispatch range = null;

        try
        {
            workbooks = getProperty(application, "Workbooks");
            workbook = invokeMethod(workbooks, "Open", new Object[] { inputFile.getAbsolutePath() });
            worksheets = getProperty(workbook, "Worksheets");
            worksheet = getProperty(worksheets, "Item", 1);
            range = getProperty(worksheet, "Range", "A1");

            // Set new value in cell
            setProperty(range, "Value", NEW_VALUE);

            File outputFile = getOutputFile(inputFile);
            if (outputFile.exists())
            {
                System.out.println("Delete existing output file: " + outputFile.getAbsolutePath());
                outputFile.delete();
            }

            // Save changes to output file
            invokeMethod(worksheet, "SaveAs", new Object[] { outputFile.getAbsolutePath() });

            System.out.println("Document saved");
        }
        finally
        {
            releaseResources(new IUnknown[] { range, worksheet, worksheets, workbook, workbooks });

            // Quit from Excel application
            invokeMethod(application, "Quit", new Object[] {});

            application.release();

            System.exit(0);
        }
    }

    private void checkInputFile(File file) throws IllegalArgumentException
    {
        if (file == null)
        {
            throw new IllegalArgumentException("Input file is null");
        }
        else if (!file.exists())
        {
            throw new IllegalArgumentException("Input file doesn't exist: " + file.getAbsolutePath());
        }
    }

    private File getOutputFile(File inputFile)
    {
        if (inputFile.getParentFile() != null)
        {
            return new File(inputFile.getParentFile(), OUTPUT_FILENAME);
        }
        else
        {
            return new File(OUTPUT_FILENAME);
        }
    }

    private static IDispatch getProperty(IDispatch obj, String propertyName)
    {
        Automation automation = new Automation(obj);
        try
        {
            IDispatchImpl pdispVal = (IDispatchImpl)automation.getProperty(propertyName).getPdispVal();
            pdispVal.setAutoDelete(false);
            return pdispVal;
        }
        finally
        {
            automation.release();
        }
    }

    private static IDispatch getProperty(IDispatch obj, String propertyName, int index)
    {
        Automation automation = new Automation(obj);
        try
        {
            IDispatchImpl pdispVal = (IDispatchImpl)automation.getProperty(propertyName, index).getPdispVal();
            pdispVal.setAutoDelete(false);
            return pdispVal;
        }
        finally
        {
            automation.release();
        }
    }

    private static IDispatch getProperty(IDispatch obj, String propertyName, String index)
    {
        Automation automation = new Automation(obj);
        try
        {
            IDispatchImpl pdispVal = (IDispatchImpl)automation.getProperty(propertyName, index).getPdispVal();
            pdispVal.setAutoDelete(false);
            return pdispVal;
        }
        finally
        {
            automation.release();
        }
    }

    private static void setProperty(IDispatch obj, String propertyName, Object val)
    {
        Automation automation = new Automation(obj);
        try
        {
            automation.setProperty(propertyName, val);
        }
        finally
        {
            automation.release();
        }
    }

    private static IDispatch invokeMethod(IDispatch obj, String methodName, Object[] params)
    {
        Automation automation = new Automation(obj);
        try
        {
            Variant result = (Variant) automation.invoke(methodName, params);
            if (result != null)
            {
                IDispatchImpl pdispVal = (IDispatchImpl)result.getPdispVal();
                pdispVal.setAutoDelete(false);
                return pdispVal;
            }
            return null;
        }
        finally
        {
            automation.release();
        }
    }

    private static void releaseResources(IUnknown[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            IUnknown unk = arr[i];
            if (unk != null)
            {
                unk.release();
            }
        }
    }

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            usage();
        }

        File inputFile = new File(args[0]);

        OleFunctions.oleInitialize();

        ExcelApplicationAutomationSample test = new ExcelApplicationAutomationSample();
        try
        {
            OleMessageLoop.invokeMethod(test, "doWork", new Object[] { inputFile });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void usage()
    {
        System.out.println(USAGE_STR);
        System.exit(0);
    }
}