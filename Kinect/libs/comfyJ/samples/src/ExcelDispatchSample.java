/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.LocaleID;
import com.jniwrapper.win32.ole.OleFunctions;
import excel.excel.*;
import excel.excel.impl.RangeImpl;
import excel.excel.impl._WorksheetImpl;

/**
 * This sample demonstrates how to interact with the Microsoft Excel application
 * using Java wrappers for COM interfaces.
 *
 * This sample requires generated stubs for COM type library:
 * Description: Microsoft Excel 11.0 Object Library
 * ProgID:      Excel.Sheet
 * GUID:        {00020813-0000-0000-C000-000000000046}
 * In the package: excel
 *
 * You can generate stubs using the Code Generator application.
 */
public class ExcelDispatchSample
{
    public static void main(String[] args) throws Exception
    {
        OleFunctions.oleInitialize();

        final Variant MISSING = Variant.createUnspecifiedParameter();
        final Int32 lcid = new Int32((int)LocaleID.LOCALE_USER_DEFAULT.getValue());

        try
        {
            _Application app = Application.create(ClsCtx.LOCAL_SERVER);
            app.setVisible(lcid, VariantBool.TRUE);
            Workbooks workbooks = app.getWorkbooks();
            _Workbook workbook = workbooks.add(MISSING, lcid);
            _Worksheet sheet = new _WorksheetImpl(workbook.getWorksheets().getItem(new Variant(1)));
            Range range = sheet.getRange(new Variant("A1"), new Variant("A1"));

            Variant indent = range.getAddIndent();
            System.out.println("indent = " + indent.getValue());
            VariantBool edit = range.getAllowEdit();
            System.out.println("edit = " + edit.getValue());
            Areas areas = range.getAreas();
            long areaCount = areas.getCount().getValue();
            System.out.println("areaCount = " + areaCount);

            Variant param1 = new Variant("val");
            range.setValue2(param1);

            Variant text = new Variant("my comment");
            Comment comment = range.addComment(text);
            BStr author = comment.getAuthor();
            System.out.println("author = " + author);

            range = sheet.getRange(new Variant("A2"), new Variant("A2"));
            range.setValue2(new Variant("A2"));

            range = sheet.getRange(new Variant("B1"), new Variant("B1"));
            range.setValue2(new Variant("B1"));

            range = sheet.getRange(new Variant("A1"), new Variant("B2"));

            Variant item = range.getItem(new Variant("1"), new Variant("B"));
            range = new RangeImpl((IDispatch) item.getValue());
            System.out.println("cell = " + range.getValue2().getValue());
        }
        finally
        {
            OleFunctions.oleUninitialize();
        }
    }
}