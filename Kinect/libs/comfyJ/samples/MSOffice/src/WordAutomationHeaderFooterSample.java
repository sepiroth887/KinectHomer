/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.system.Kernel32;

import java.lang.reflect.InvocationTargetException;

/**
 * This class demonstrates how to add header and footer to the Word document using Automation.
 *
 * @author Oleg Yakovskiy
 */
public class WordAutomationHeaderFooterSample
{
    private static final String WORD_APPLICATION_PROGID = "Word.Application";
    private static final String PATH = Kernel32.expandEnvironmentStrings("%HOMEDRIVE%") +
                                       Kernel32.expandEnvironmentStrings("%HOMEPATH%") + "\\TestWord.doc";

    private static final int WD_SEEK_MAIN_DOCUMENT = 0;
    private static final int WD_SEEK_CURRENT_PAGE_HEADER = 9;
    private static final int WD_SEEK_CURRENT_PAGE_FOOTER = 10;

    /**
     * Creates Word document, adds header and footer, and saves document to the drive
     *
     * @param 
     */
    public static void main(String[] args)
    {
        // Initialize OLE
        OleFunctions.oleInitialize();

        // Create Word document
        IDispatch wordDispatch = new IDispatchImpl(CLSID.createFromProgID(WORD_APPLICATION_PROGID), ClsCtx.LOCAL_SERVER);
        Automation word = new Automation(wordDispatch , true);
        Automation documents = new Automation((IDispatch)word.getProperty("Documents").getActiveMember(), true);
        documents.invoke("Add");

        // Initialize automation objects
        Automation selection = new Automation((IDispatch)word.getProperty("Selection").getActiveMember(), true);
        Automation activeDocument = new Automation((IDispatch)word.getProperty("ActiveDocument").getActiveMember(), true);
        Automation activeWindow = new Automation((IDispatch)word.getProperty("ActiveWindow").getActiveMember(), true);
        Automation activePan = new Automation((IDispatch)activeWindow.getProperty("ActivePane").getActiveMember(), true);
        Automation view = new Automation((IDispatch)activePan.getProperty("View").getActiveMember(), true);

        // Create header and footer of the document
        view.setProperty("SeekView", new Variant(WD_SEEK_CURRENT_PAGE_HEADER));
        selection.invoke("TypeText", "Header test");

        view.setProperty("SeekView", new Variant(WD_SEEK_CURRENT_PAGE_FOOTER));
        selection.invoke("TypeText", "Footer test");

        view.setProperty("SeekView", new Variant(WD_SEEK_MAIN_DOCUMENT));

        // Save document to the drive
        activeDocument.invoke("SaveAs", PATH);
        word.invoke("Quit");

        // Release resources
        view.release();
        activePan.release();
        activeWindow.release();
        activeDocument.release();
        selection.release();
        documents.release();
        word.release();

        // Uninitialize OLE
        OleFunctions.oleUninitialize();
   }
 }
