/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package operations;

import com.jniwrapper.*;
import com.jniwrapper.util.Logger;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.PageRange;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.PrintHandler;
import com.jniwrapper.win32.automation.PrinterDeviceInfo;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.server.IUnknownServer;
import com.jniwrapper.win32.gdi.PrintParameters;
import com.jniwrapper.win32.ole.IPreviewCallback;
import com.jniwrapper.win32.ole.PageSet;
import com.jniwrapper.win32.ole.impl.IInPlacePrintPreviewImpl;
import com.jniwrapper.win32.ole.impl.IPreviewCallbackImpl;
import com.jniwrapper.win32.ole.impl.IPrintImpl;
import com.jniwrapper.win32.ole.server.IPreviewCallbackVTBL;
import com.jniwrapper.win32.system.WinNT;
import com.jniwrapper.win32.ui.dialogs.PrintDialog;

import java.awt.*;

/**
 *
 * @author Alexei Orischenko
 */
public class OfficePrintHandler implements PrintHandler
{
    private static final Logger LOG = Logger.getInstance(OfficePrintHandler.class);

    private static IClassFactoryServer PREVIEW_CALLBACK_CLASS_FACTORY_SERVER;
    private static final IClassFactory PREVIEW_CALLBACK_FACTORY = createPreviewCallbackFactory();

    private static final int NOTIFY_FINISHED = 1;
    private static final int NOTIFY_FORCECLOSEPREVIEW = 32;
    private static final int NOTIFY_UNABLETOPREVIEW = 128;

    private static final int PRINTFLAG_RECOMPOSETODEVICE = 8;

    private OleContainer _oleContainer;
    private boolean _inPrintPreview = false;

    private static IClassFactory createPreviewCallbackFactory()
    {
        PREVIEW_CALLBACK_CLASS_FACTORY_SERVER = new IClassFactoryServer(PreviewCallback.class);
        PREVIEW_CALLBACK_CLASS_FACTORY_SERVER.registerInterface(IPreviewCallback.class, new IPreviewCallbackVTBL(PREVIEW_CALLBACK_CLASS_FACTORY_SERVER));
        PREVIEW_CALLBACK_CLASS_FACTORY_SERVER.setDefaultInterface(IPreviewCallback.class);

        return PREVIEW_CALLBACK_CLASS_FACTORY_SERVER.createIClassFactory();
    }

    public OfficePrintHandler()
    {
    }

    public void setContainer(OleContainer container)
    {
        _oleContainer = container;
    }

    public void print()
    {
        print(1, 1, 1);
    }

    public void print(int startPage, int endPage, int numCopies)
    {
        final PrintParameters printParameters = PrintParameters.create(startPage, endPage, numCopies);

        print(printParameters);
    }

    /**
     * Displays a print dialog box.
     *
     * @since 2.5
     */
    public void print(final PrintParameters printParameters)
    {
        final IPrintImpl print = new IPrintImpl();
        print.setAutoDelete(false);

        try
        {
            final PrinterDeviceInfo deviceInfo = getPrintInfo(getParentWindow(), printParameters);

            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    IPrintImpl print = new IPrintImpl();
                    print.setAutoDelete(false);

                    _oleContainer.getOleObject().queryInterface(print.getIID(), print);

                    if (deviceInfo != null)
                    {
                        Handle lpTargetDevice = deviceInfo.createPrintDevice();
                        Handle pageSet = createPageSet(printParameters);
                        doPrint(lpTargetDevice, pageSet, print);
                    }

                    print.release();
                }
            };

            try
            {
                OleMessageLoop.invokeMethod(runnable, "run");
            }
            catch (Exception e)
            {
                LOG.error("", e);
            }
        }
        catch (ComException e)
        {
            LOG.error("Can't print office document.", e);
        }
    }

    private PrinterDeviceInfo getPrintInfo(Window printDialogOwner, PrintParameters printParameters)
    {
        PrinterDeviceInfo result = new PrinterDeviceInfo();

        PrintDialog printDialog = new PrintDialog(printDialogOwner);

        printDialog.setCopies(printParameters.getNumCopies());
        printDialog.setMinPage(printParameters.getMinPage());
        printDialog.setMaxPage(printParameters.getMaxPage());
        printDialog.setFromPage(printParameters.getFromPage());
        printDialog.setToPage(printParameters.getToPage());

        if (printDialog.open())
        {
            result.setDevNames(printDialog.getDevNames());
            result.setDevMode(printDialog.getDevMode());
            result.setNumCopies(printDialog.getCopies());

            return result;
        }
        else
        {
            return null;
        }
    }

    private Handle createPageSet(PrintParameters printParameters)
    {
        PageSet pageSet = new PageSet();

        int structureLength = pageSet.getLength();
        final Pointer.Void lpPageSet = ComFunctions.coTaskMemAlloc(new ULongInt(structureLength));
        WinNT.zeroMemory(lpPageSet, structureLength);
        Pointer ptrPageSet = new Pointer(pageSet);
        lpPageSet.castTo(ptrPageSet);

        pageSet.setCbStruct(structureLength);
        pageSet.setOddPages(printParameters.isPrintOddPages());
        pageSet.setEvenPages(printParameters.isPrintEvenPages());
        pageSet.setPageRange(printParameters.getPageRanges().length);

        for (int i = 0; i < printParameters.getPageRanges().length; i++)
        {
            PageRange pageRange = printParameters.getPageRanges()[i];

            PageRange pageRangeForPageSet = (PageRange)pageSet.getPages().getElement(i);
            pageRangeForPageSet.setFromPage(pageRange.getFromPage());
            pageRangeForPageSet.setToPage(pageRange.getToPage());
        }

        return new Handle(lpPageSet.getValue());
    }

    private void doPrint(Handle lpTargetDevice, Handle lpPageSet, IPrintImpl print)
    {
        int pagesPrinted = 0;
        int lastPage = 0;

        print.setInitialPageNum(new LongInt(1));

        print.print(new Int32(PRINTFLAG_RECOMPOSETODEVICE),
                lpTargetDevice,
                lpPageSet,
                null,
                null,
                new Int32(1),
                new Int32(pagesPrinted),
                new Int32(lastPage));
    }

    public void printPreview()
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                final IInPlacePrintPreviewImpl preview = new IInPlacePrintPreviewImpl();
                preview.setAutoDelete(false);
                _oleContainer.getOleObject().queryInterface(preview.getIID(), preview);

                preview.queryStatus();

                IPreviewCallbackImpl previewCallback = new IPreviewCallbackImpl();
                previewCallback.setAutoDelete(false);
                PREVIEW_CALLBACK_FACTORY.createInstance(null, previewCallback.getIID(), previewCallback);
                PreviewCallback previewCallbackInstance = (PreviewCallback)PREVIEW_CALLBACK_CLASS_FACTORY_SERVER.getInstances().pop();
                previewCallbackInstance.setPrintHandler(OfficePrintHandler.this);

                preview.startPrintPreview(new Int32(1), null, previewCallback, new Int32(1));
                _inPrintPreview = true;

                previewCallback.release();
                preview.release();
            }
        };

        try
        {
            OleMessageLoop.invokeMethod(runnable, "run");
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }
    }

    public void closePrintPreview()
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                exitPrintPreview(true);
            }
        };

        try
        {
            OleMessageLoop.invokeMethod(runnable, "run");
        }
        catch (Exception e)
        {
            LOG.error("", e);
        }
    }

    private void exitPrintPreview(boolean force)
    {
        _inPrintPreview = false;

        if (force)
        {
            IInPlacePrintPreviewImpl preview = new IInPlacePrintPreviewImpl();
            preview.setAutoDelete(false);

            _oleContainer.getOleObject().queryInterface(preview.getIID(), preview);

            preview.endPrintPreview(new VariantBool(true));
            preview.release();
        }
    }

    public boolean isInPrintPreview()
    {
        return _inPrintPreview;
    }

    public static class PreviewCallback extends IUnknownServer
        implements IPreviewCallback
    {
        private OfficePrintHandler _printHandler;

        public PreviewCallback(CoClassMetaInfo classImpl)
        {
            super(classImpl);
        }

        public void setPrintHandler(OfficePrintHandler helper)
        {
            _printHandler = helper;
        }

        public void Notify(Int32 wStatus, Int32 nLastPage, WideString previewStatus)
        {
            if (wStatus.getValue() == NOTIFY_FORCECLOSEPREVIEW ||
                    wStatus.getValue() == NOTIFY_FINISHED ||
                    wStatus.getValue() == NOTIFY_UNABLETOPREVIEW)
            {
                _printHandler.exitPrintPreview(true);
            }
        }
    }

    private Window getParentWindow()
    {
        Component parent = _oleContainer.getParent();
        while (parent != null && !(parent instanceof Window))
        {
            parent = parent.getParent();
        }

        return (Window)parent;
    }
}