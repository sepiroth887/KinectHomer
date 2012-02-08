/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.ole.OleFunctions;
import word.word.Application;
import word.word.WdSaveFormat;
import word.word._Application;
import word.word._Document;

/**
 * This sample demonstrates how to save a Word document into the XML format.
 *
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Word 11.0 Object Library
 * ProgID:      Word.Document
 * GUID:        {00020905-0000-0000-C000-000000000046}
 * In the package: word
 *
 * You can generate stubs using the Code Generator application.
 */
public class WordXmlSample
{
    public static void main(String[] args)
    {
        OleFunctions.oleInitialize();

        _Application app = Application.create(ClsCtx.LOCAL_SERVER);
        app.setVisible(new VariantBool(true));

        try
        {
            _Document doc = app.getDocuments().add(new Variant("e:\\test.xml"),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter());

            doc.saveAs(new Variant("e:\\output.xml"),
                    new Variant(WdSaveFormat.wdFormatXML),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter());

            System.out.println("Document saved successfully.");
        }
        finally
        {
            app.quit(Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter(),
                    Variant.createUnspecifiedParameter());
        }
    }
}