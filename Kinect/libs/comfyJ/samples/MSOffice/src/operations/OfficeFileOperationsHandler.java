/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package operations;

import com.jniwrapper.win32.automation.FileOperationsHandler;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.ui.dialogs.OpenSaveFileDialog;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.Map;
import java.util.HashMap;

/**
 * This class enables open / save operations for office documents embedded into OleContainer.
 *
 * <p>Usage.
 *      <code><pre>
 *          OleContainer container = new OleContainer();
 *          container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_EXCEL));
 *
 *          container.createObject("Excel.Sheet");
 *          container.doVerb(OleVerbs.SHOW);
 *      </pre></code>
 * 
 * @author Alexei Orischenko
 */
public class OfficeFileOperationsHandler implements FileOperationsHandler
{
    public static final int TYPE_EXCEL = 1;
    public static final int TYPE_WORD = 2;
    public static final int TYPE_POWERPOINT = 3;

    private static Map _filters = new HashMap();
    private static Map _extensions = new HashMap();

    static {
        _filters.put(new Integer(TYPE_EXCEL), "Excel File | *.xls");
        _filters.put(new Integer(TYPE_WORD), "Word Document | *.doc");
        _filters.put(new Integer(TYPE_POWERPOINT), "PowerPoint Presentation | *.ppt");

        _extensions.put(new Integer(TYPE_EXCEL), "xls");
        _extensions.put(new Integer(TYPE_WORD), "doc");
        _extensions.put(new Integer(TYPE_POWERPOINT), "ppt");
    }

    private OleContainer _container;
    private Integer _documentType = new Integer(TYPE_EXCEL);
    private static final String MSG_INCORRECT_TYPE = "Incorrect type of document: type should be equals to TYPE_EXCEL, TYPE_WORD or TYPE_POWERPOINT";

    /**
     * Creates handler for specified type of office document.
     *
     * @param documentType one of TYPE_ constants (for examle, TYPE_EXCEL)
     */
    public OfficeFileOperationsHandler(int documentType)
    {
        checkType(documentType);
        _documentType = new Integer(documentType);
    }

    private void checkType(int type)
    {
        if (type != TYPE_EXCEL  &&
            type != TYPE_WORD &&
            type != TYPE_POWERPOINT)
        {
            throw new RuntimeException(MSG_INCORRECT_TYPE);
        }
    }

    public void setContainer(OleContainer container)
    {
        _container = container;
    }

    /**
     * Returns file that should be opened.
     *
     * <p>This method is executed when "Open" button is pressed.
     *
     * @return returns file that should be opened or null if operation is cancelled.
     */
    public File getOpenFile()
    {
        OpenSaveFileDialog dialog = new OpenSaveFileDialog(getParentWindow());
        dialog.setFilter(getFilter());

        if (dialog.getOpenFileName())
        {
            return new File(dialog.getFileName());
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns file for saving document.
     *
     * <p>This method is executed when "Save" button is pressed.
     *
     * @return file for saving document or null if operation is cancelled.
     */
    public File getSaveFile()
    {
        OpenSaveFileDialog dialog = new OpenSaveFileDialog(getParentWindow());

        dialog.setFilter(getFilter());
        dialog.setDefaultExt(getExtension());

        if (dialog.getSaveFileName())
        {
            return new File(dialog.getFileName());
        }
        else
        {
            return null;
        }
    }

    /**
     * This method is called when new document is created.
     */
    public void newDocumentCreated()
    {
    }

    /**
     * This method is called when document is opened.
     */
    public void documentOpened(File file)
    {
    }

    /**
     * This method is called when document is saved.
     */
    public void documentSaved(File file)
    {
    }

    private String getExtension()
    {
        return (String)_extensions.get(_documentType);
    }

    private String getFilter()
    {
        return (String)_filters.get(_documentType);
    }

    private Window getParentWindow()
    {
        Component parent = _container.getParent();
        while (parent != null && !(parent instanceof Window))
        {
            parent = parent.getParent();
        }

        return (Window)parent;
    }
}