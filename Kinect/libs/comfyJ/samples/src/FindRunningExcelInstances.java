import com.jniwrapper.win32.ui.Wnd;

import java.util.List;
import java.util.Iterator;

import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.ole.OleFunctions;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.Library;
import com.jniwrapper.Function;
import com.jniwrapper.UInt32;
import com.jniwrapper.Pointer;

/**
 * This sample enumerates all Excel instances and print active Workbooks names
 */
public class FindRunningExcelInstances
{
    static final String FUNCTION_ACCESSIBLE_OBJECT_FROM_WINDOW = "AccessibleObjectFromWindow";
    static Function _accessibleObjectFromWindow;

    /**
     * This sample outputs all names of currently running Excel worksheets 
     */
    public static void main(String[] args) {
        // Initialize OLE
        OleFunctions.oleInitialize();

        // Find and initialize AccessibleObjectFromWindow function
        final Library oleacc = new Library("oleacc");
        _accessibleObjectFromWindow = oleacc.getFunction(FUNCTION_ACCESSIBLE_OBJECT_FROM_WINDOW);

        boolean excelInstanceWasFound = false;

        // Get all windows in the system
        List allWindows = Wnd.getAllWindows();
        for (Iterator iteratorWnd = allWindows.iterator(); iteratorWnd.hasNext();)
        {
            Wnd wndUpper = (Wnd) iteratorWnd.next();
            if (wndUpper.getWindowClassName().equals("XLMAIN"))
            {
                // We've found main Excel window, let's enumerate it's children
                for (Iterator iterator = wndUpper.getChildWindows().iterator(); iterator.hasNext();)
                {
                    Wnd wnd = (Wnd) iterator.next();
                    if (wnd.getWindowClassName().equals("EXCEL7"))
                    {
                        // We've found Excel window
                        final HResult hres = new HResult();
                        final IUnknownImpl resInterface = new IUnknownImpl();
                        _accessibleObjectFromWindow.invoke(hres,
                            wnd,
                            new UInt32(0xFFFFFFF0L), // OBJID_NATIVEOM
                            new Pointer.Const(new IID(IDispatch.INTERFACE_IDENTIFIER)),
                            new Pointer.OutOnly(resInterface)
                        );

                        if (!hres.isError() && !resInterface.isNull())
                        {
                            excelInstanceWasFound = true;

                            // NOTE: Code below can be re-written to use Excel COM stubs,
                            // this will make it more clean and simple. We use Automation
                            // just to keep dependencies minimal.

                            // Excel returns Window object from AccessibleObjectFromWindow 
                            Automation instance = new Automation(resInterface, true);
                            // Get ActiveSheet from this Window
                            Automation sheet = new Automation(instance.getProperty("ActiveSheet").getPdispVal(), true);
                            // Get worksheet name
                            BStr wsName = sheet.getProperty("Name").getBstrVal();
                            // Get Parent (say, Worksheet) from the Sheet
                            Automation wb = new Automation(sheet.getProperty("Parent").getPdispVal(), true);
                            // Get workbook name
                            BStr wbName = wb.getProperty("Name").getBstrVal();
                            // Print workbook name 
                            System.out.println("Workbook: " + wbName.getValue() + " [" + wsName.getValue() + "]");
                        }
                    }
                }
            }
        }

        if (!excelInstanceWasFound)
        {
            System.out.println("No running Excel workbooks has been found.");
        }
    }
}
