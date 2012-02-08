import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import excel.excel.*;
import excel.excel.impl.IAppEventsImpl;
import excel.excel.impl._WorkbookImpl;
import excel.excel.server.AppEventsServer;
import excel.office.MsoSyncEventType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExcelAppEventsHandling extends JFrame
{
    private IClassFactoryServer classFactoryServer;
    private OleContainer container;

    private _Application application;
    private IConnectionPoint connectionPoint;
    private Int32 eventsHandlerIdentifier;

    public ExcelAppEventsHandling()
    {
        super("Excel events handling sample");

        container = new OleContainer();
        container.createObject("Excel.Sheet");

        getContentPane().add(container, BorderLayout.CENTER);
        installEventsHandler();

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.out.println("windowClosing");
                release();
            }
        });
    }

    private void release()
    {
        try
        {
            container.getOleMessageLoop().doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    connectionPoint.unadvise(eventsHandlerIdentifier);
                    connectionPoint.setAutoDelete(false);
                    connectionPoint.release();
                    container.destroyObject();
                    application.quit();
                    application.setAutoDelete(false);
                    application.release();
                }
            });
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    private void installEventsHandler()
    {
        try
        {
            container.getOleMessageLoop().doInvokeAndWait(new Runnable()
            {
                public void run()
                {
                    _WorkbookImpl workbook = new _WorkbookImpl(container.getOleObject());

                    classFactoryServer = new IClassFactoryServer(AppEventsHandler.class);
                    classFactoryServer.registerInterface(IAppEvents.class, new IDispatchVTBL(classFactoryServer));
                    classFactoryServer.setDefaultInterface(IDispatch.class);

                    IClassFactory classFactory = classFactoryServer.createIClassFactory();
                    IAppEventsImpl appEventsImpl = new IAppEventsImpl();
                    classFactory.createInstance(null, appEventsImpl.getIID(), appEventsImpl);

                    application = workbook.getApplication();

                    IConnectionPointContainer cpc = new IConnectionPointContainerImpl(application);

                    connectionPoint = cpc.findConnectionPoint(new IID(AppEvents.INTERFACE_IDENTIFIER));

                    eventsHandlerIdentifier = connectionPoint.advise(appEventsImpl);

                    classFactory.setAutoDelete(false);

                    workbook.setAutoDelete(false);
                    workbook.release();

                    cpc.setAutoDelete(false);
                    cpc.release();
                }
            });
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static class AppEventsHandler extends AppEventsServer
    {
        public AppEventsHandler(CoClassMetaInfo classImpl)
        {
            super(classImpl);
            registerMethods(AppEvents.class, false);
            System.out.println("AppEventsHandler.<init>");
        }

        public void windowResize(_Workbook Wb, excel.excel.Window Wn) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.windowResize");
        }

        public void windowActivate(_Workbook Wb, excel.excel.Window Wn) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.windowActivate");
        }

        public void windowDeactivate(_Workbook Wb, excel.excel.Window Wn) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.windowDeactivate");
        }

        public void newWorkbook(_Workbook Wb) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.newWorkbook");
        }

        public void sheetSelectionChange(IDispatch Sh, Range Target) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetSelectionChange");
        }

        public void sheetBeforeDoubleClick(IDispatch Sh, Range Target, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetBeforeDoubleClick");
        }

        public void sheetBeforeRightClick(IDispatch Sh, Range Target, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetBeforeRightClick");
        }

        public void sheetActivate(IDispatch Sh) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetActivate");
        }

        public void sheetDeactivate(IDispatch Sh) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetDeactivate");
        }

        public void sheetCalculate(IDispatch Sh) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetCalculate");
        }

        public void sheetChange(IDispatch Sh, Range Target) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetChange");
        }

        public void workbookOpen(_Workbook Wb) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookOpen");
        }

        public void workbookActivate(_Workbook Wb) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookActivate");
        }

        public void workbookDeactivate(_Workbook Wb) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookDeactivate");
        }

        public void workbookBeforeClose(_Workbook Wb, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookBeforeClose");
        }

        public void workbookBeforeSave(_Workbook Wb, VariantBool SaveAsUI, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookBeforeSave");
        }

        public void workbookBeforePrint(_Workbook Wb, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookBeforePrint");
        }

        public void workbookNewSheet(_Workbook Wb, IDispatch Sh) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookNewSheet");
        }

        public void workbookAddinInstall(_Workbook Wb) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookAddinInstall");
        }

        public void workbookAddinUninstall(_Workbook Wb) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookAddinUninstall");
        }

        public void sheetFollowHyperlink(IDispatch Sh, Hyperlink Target) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetFollowHyperlink");
        }

        public void sheetPivotTableUpdate(IDispatch Sh, PivotTable Target) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.sheetPivotTableUpdate");
        }

        public void workbookPivotTableCloseConnection(_Workbook Wb, PivotTable Target) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookPivotTableCloseConnection");
        }

        public void workbookPivotTableOpenConnection(_Workbook Wb, PivotTable Target) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookPivotTableOpenConnection");
        }

        public void workbookSync(_Workbook Wb, MsoSyncEventType SyncEventType) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookSync");
        }

        public void workbookBeforeXmlImport(_Workbook Wb, XmlMap Map, BStr Url, VariantBool IsRefresh, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookBeforeXmlImport");
        }

        public void workbookAfterXmlImport(_Workbook Wb, XmlMap Map, VariantBool IsRefresh, XlXmlImportResult Result) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookAfterXmlImport");
        }

        public void workbookBeforeXmlExport(_Workbook Wb, XmlMap Map, BStr Url, VariantBool Cancel) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookBeforeXmlExport");
        }

        public void workbookAfterXmlExport(_Workbook Wb, XmlMap Map, BStr Url, XlXmlExportResult Result) throws ComException
        {
            System.out.println("ExcelAppEventsHandling$AppEventsHandler.workbookAfterXmlExport");
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                ExcelAppEventsHandling powerPointEventsHandling = new ExcelAppEventsHandling();
                powerPointEventsHandling.setSize(800, 600);
                powerPointEventsHandling.setLocationRelativeTo(null);
                powerPointEventsHandling.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                powerPointEventsHandling.setVisible(true);
            }
        });
    }
}
