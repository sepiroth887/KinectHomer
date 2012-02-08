/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Parameter;
import com.jniwrapper.SingleFloat;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.ole.types.OleVerbs;
import excel.excel.impl.RangeImpl;
import operations.OfficeFileOperationsHandler;
import operations.OfficePrintHandler;
import word.word.Range;
import word.word._Font;
import word.word.impl._DocumentImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * This sample demonstrates the technique of embedding Word application into a java application
 * using OleContainer and generated java stubs for Word application.
 * <p/>
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Word 11.0 Object Library
 * ProgID:      Word.Document
 * GUID:        {00020905-0000-0000-C000-000000000046}
 * In the package: word
 * <p/>
 * You can generate stubs using the Code Generator application.
 *
 * @author Alexei Orischenko
 */
public class WordIntegrationSample extends JFrame {
    private static final Dimension WINDOW_SIZE = new Dimension(720, 480);

    /**
     * progid of word document
     */
    private static final String DOCUMENT_PROGID = "Word.Document";

    private OleContainer _container;

    public WordIntegrationSample() {
        super("JNIWrapper - Word Integration");

        _container = new OleContainer();
        _container.createObject(DOCUMENT_PROGID);

        getContentPane().add(_container, BorderLayout.CENTER);

        // Enable open / save operations
        _container.setFileOperationsHandler(new OfficeFileOperationsHandler(OfficeFileOperationsHandler.TYPE_WORD));

        // Enable print / print preview
        _container.setPrintDocumentHandler(new OfficePrintHandler());
    }

    public void modifyDocument() {
        // get word document from container object
        _DocumentImpl document = new _DocumentImpl(_container.getOleObject());
        document.setAutoDelete(false);

//        _Application application = document.getApplication();
//        application.setVisible(new VariantBool(true));
        try {
            // insert text to begin of word document
            insertText(document, 0, "You can create word documents using JNIWrapper!", 20);
            int documentLength = getDocumentLength(document);
            insertText(document, documentLength - 1, "And you can type text in editor...", 12);
        }
        finally {
            document.release();
        }
    }

    private int getDocumentLength(_DocumentImpl document) {
        Range content = document.getContent();
        try {
            return (int) content.getEnd().getValue();
        }
        finally {
            content.setAutoDelete(false);
            content.release();
        }
    }

    private void insertText(_DocumentImpl document, int pos, String text, int fontSize) {
        RangeImpl prop = new RangeImpl();
        document.invokeStandardVirtualMethod(178, (byte) 2, new Parameter[]{
                new com.jniwrapper.Pointer.Const(new Variant(pos)),
                new com.jniwrapper.Pointer.Const(new Variant(pos)),
                new com.jniwrapper.Pointer.OutOnly(prop)
        });

        Range range = document.range(new Variant(pos), new Variant(pos));
        try {
            range.setText(new BStr(text));
            _Font font = range.getFont();
            try {
                font.setSize(new SingleFloat(fontSize));
            }
            finally {
                font.setAutoDelete(false);
                font.release();
            }
            range.insertParagraphAfter();
        }
        finally {
            range.setAutoDelete(false);
            range.release();
        }
    }

    private static void createGUI() {
        final WordIntegrationSample app = new WordIntegrationSample();
        app.setSize(WINDOW_SIZE);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        app.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                // show word
                app._container.doVerb(OleVerbs.INPLACEACTIVATE);
                // word with word using generated sources
                try {
                    app._container.getOleMessageLoop().doInvokeMethod(app, "modifyDocument", new Object[]{});
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void windowClosing(WindowEvent e) {
                // close word document on exit
                app._container.destroyObject();
            }
        });
        app.setVisible(true);
    }

    public static void main(String[] args) {
        createGUI();
    }
}