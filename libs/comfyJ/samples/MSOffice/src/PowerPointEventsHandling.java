import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import point.office.MsoSyncEventType;
import point.powerpoint.*;
import point.powerpoint.impl.EApplicationImpl;
import point.powerpoint.impl._PresentationImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PowerPointEventsHandling extends JFrame
{
    private IClassFactoryServer classFactoryServer;
    private OleContainer container;

    private _Application application;
    private IConnectionPoint connectionPoint;
    private Int32 eventsHandlerIdentifier;

    public PowerPointEventsHandling()
    {
        super("PowerPoint events handling sample");

        container = new OleContainer();
        container.createObject("Powerpoint.Show");

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
                    _PresentationImpl presentation = new _PresentationImpl(container.getOleObject());

                    classFactoryServer = new IClassFactoryServer(PowerPointEventHandler.class);
                    classFactoryServer.registerInterface(EApplication.class, new IDispatchVTBL(classFactoryServer));
                    classFactoryServer.setDefaultInterface(IDispatch.class);

                    IClassFactory classFactory = classFactoryServer.createIClassFactory();

                    EApplicationImpl eApplication = new EApplicationImpl();
                    classFactory.createInstance(null, eApplication.getIID(), eApplication);

                    application = presentation.getApplication();

                    IConnectionPointContainer cpc = new IConnectionPointContainerImpl(application);

                    connectionPoint = cpc.findConnectionPoint(new IID(EApplication.INTERFACE_IDENTIFIER));

                    eventsHandlerIdentifier = connectionPoint.advise(eApplication);

                    classFactory.setAutoDelete(false);

                    presentation.setAutoDelete(false);
                    presentation.release();

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

    /*
        [
          odl,
          uuid(914934C2-5A91-11CF-8700-00AA0060263B),
          helpcontext(0x000979c8)
        ]
        interface EApplication : IDispatch {
            [helpcontext(0x000979c9)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2001)]
            HRESULT _stdcall WindowSelectionChange([in] Selection* Sel);
            [helpcontext(0x000979ca)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2002)]
            HRESULT _stdcall WindowBeforeRightClick(
                            [in] Selection* Sel,
                            [in, out] VARIANT_BOOL* Cancel);
            [helpcontext(0x000979cb)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2003)]
            HRESULT _stdcall WindowBeforeDoubleClick(
                            [in] Selection* Sel,
                            [in, out] VARIANT_BOOL* Cancel);
            [helpcontext(0x000979cc)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2004)]
            HRESULT _stdcall PresentationClose([in] Presentation* Pres);
            [helpcontext(0x000979cd)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2005)]
            HRESULT _stdcall PresentationSave([in] Presentation* Pres);
            [helpcontext(0x000979ce)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2006)]
            HRESULT _stdcall PresentationOpen([in] Presentation* Pres);
            [helpcontext(0x000979cf)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2007)]
            HRESULT _stdcall NewPresentation([in] Presentation* Pres);
            [helpcontext(0x000979d0)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2008)]
            HRESULT _stdcall PresentationNewSlide([in] Slide* Sld);
            [helpcontext(0x000979d1)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2009)]
            HRESULT _stdcall WindowActivate(
                            [in] Presentation* Pres,
                            [in] DocumentWindow* Wn);
            [helpcontext(0x000979d2)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2010)]
            HRESULT _stdcall WindowDeactivate(
                            [in] Presentation* Pres,
                            [in] DocumentWindow* Wn);
            [helpcontext(0x000979d3)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2011)]
            HRESULT _stdcall SlideShowBegin([in] SlideShowWindow* Wn);
            [helpcontext(0x000979d4)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2012)]
            HRESULT _stdcall SlideShowNextBuild([in] SlideShowWindow* Wn);
            [helpcontext(0x000979d5)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2013)]
            HRESULT _stdcall SlideShowNextSlide([in] SlideShowWindow* Wn);
            [helpcontext(0x000979d6)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2014)]
            HRESULT _stdcall SlideShowEnd([in] Presentation* Pres);
            [helpcontext(0x000979d7)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2015)]
            HRESULT _stdcall PresentationPrint([in] Presentation* Pres);
            [helpcontext(0x000979d8)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2016)]
            HRESULT _stdcall SlideSelectionChanged([in] SlideRange* SldRange);
            [helpcontext(0x000979d9)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2017)]
            HRESULT _stdcall ColorSchemeChanged([in] SlideRange* SldRange);
            [helpcontext(0x000979da)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2018)]
            HRESULT _stdcall PresentationBeforeSave(
                            [in] Presentation* Pres,
                            [in, out] VARIANT_BOOL* Cancel);
            [helpcontext(0x000979db)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2019)]
            HRESULT _stdcall SlideShowNextClick(
                            [in] SlideShowWindow* Wn,
                            [in] Effect* nEffect);
            [helpcontext(0x000979dc)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2020)]
            HRESULT _stdcall AfterNewPresentation([in] Presentation* Pres);
            [helpcontext(0x000979dd)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2021)]
            HRESULT _stdcall AfterPresentationOpen([in] Presentation* Pres);
            [helpcontext(0x000979de)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2022)]
            HRESULT _stdcall PresentationSync(
                            [in] Presentation* Pres,
                            [in] MsoSyncEventType SyncEventType);
            [helpcontext(0x000979df)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2023)]
            HRESULT _stdcall SlideShowOnNext([in] SlideShowWindow* Wn);
            [helpcontext(0x000979e0)          custom(CD2BC5C9-F452-4326-B714-F9C539D4DA58, 2024)]
            HRESULT _stdcall SlideShowOnPrevious([in] SlideShowWindow* Wn);
        };
     */
    public interface PowerPointMappedEvents extends EApplication
    {
        // these constants are taken from IDL declaration of EApplication interface above
        final static int DISPID_windowSelectionChange = 2001;
        final static int DISPID_windowBeforeRightClick = 2002;
        final static int DISPID_windowBeforeDoubleClick = 2003;
        final static int DISPID_presentationClose = 2004;
        final static int DISPID_presentationSave = 2005;
        final static int DISPID_presentationOpen = 2006;
        final static int DISPID_newPresentation = 2007;
        final static int DISPID_presentationNewSlide = 2008;
        final static int DISPID_windowActivate = 2009;
        final static int DISPID_windowDeactivate = 2010;
        final static int DISPID_slideShowBegin = 2011;
        final static int DISPID_slideShowNextBuild = 2012;
        final static int DISPID_slideShowNextSlide = 2013;
        final static int DISPID_slideShowEnd = 2014;
        final static int DISPID_presentationPrint = 2015;
        final static int DISPID_slideSelectionChanged = 2016;
        final static int DISPID_colorSchemeChanged = 2017;
        final static int DISPID_presentationBeforeSave = 2018;
        final static int DISPID_slideShowNextClick = 2019;
        final static int DISPID_afterNewPresentation = 2020;
        final static int DISPID_afterPresentationOpen = 2021;
        final static int DISPID_presentationSync = 2022;

        void windowSelectionChange(point.powerpoint.Selection selection) throws com.jniwrapper.win32.com.ComException;

        void windowBeforeRightClick(point.powerpoint.Selection selection, com.jniwrapper.win32.automation.types.VariantBool variantBool) throws com.jniwrapper.win32.com.ComException;

        void windowBeforeDoubleClick(point.powerpoint.Selection selection, com.jniwrapper.win32.automation.types.VariantBool variantBool) throws com.jniwrapper.win32.com.ComException;

        void presentationClose(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void presentationSave(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void presentationOpen(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void newPresentation(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void presentationNewSlide(point.powerpoint._Slide slide) throws com.jniwrapper.win32.com.ComException;

        void windowActivate(point.powerpoint._Presentation presentation, point.powerpoint.DocumentWindow documentWindow) throws com.jniwrapper.win32.com.ComException;

        void windowDeactivate(point.powerpoint._Presentation presentation, point.powerpoint.DocumentWindow documentWindow) throws com.jniwrapper.win32.com.ComException;

        void slideShowBegin(point.powerpoint.SlideShowWindow slideShowWindow) throws com.jniwrapper.win32.com.ComException;

        void slideShowNextBuild(point.powerpoint.SlideShowWindow slideShowWindow) throws com.jniwrapper.win32.com.ComException;

        void slideShowNextSlide(point.powerpoint.SlideShowWindow slideShowWindow) throws com.jniwrapper.win32.com.ComException;

        void slideShowEnd(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void presentationPrint(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void slideSelectionChanged(point.powerpoint.SlideRange slideRange) throws com.jniwrapper.win32.com.ComException;

        void colorSchemeChanged(point.powerpoint.SlideRange slideRange) throws com.jniwrapper.win32.com.ComException;

        void presentationBeforeSave(point.powerpoint._Presentation presentation, com.jniwrapper.win32.automation.types.VariantBool variantBool) throws com.jniwrapper.win32.com.ComException;

        void slideShowNextClick(point.powerpoint.SlideShowWindow slideShowWindow, point.powerpoint.Effect effect) throws com.jniwrapper.win32.com.ComException;

        void afterNewPresentation(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void afterPresentationOpen(point.powerpoint._Presentation presentation) throws com.jniwrapper.win32.com.ComException;

        void presentationSync(point.powerpoint._Presentation presentation, point.office.MsoSyncEventType msoSyncEventType) throws com.jniwrapper.win32.com.ComException;
    }

    public static class PowerPointEventHandler extends com.jniwrapper.win32.com.server.IDispatchServer implements PowerPointMappedEvents
    {
        public PowerPointEventHandler(CoClassMetaInfo classImpl)
        {
            super(classImpl);
            registerMethods(PowerPointMappedEvents.class, true);
            System.out.println("PowerPointEventHandler.<init>");
        }

        public void windowSelectionChange(Selection selection) throws ComException
        {
            System.out.println("PowerPointEventHandler.windowSelectionChange");
        }

        public void windowBeforeRightClick(Selection selection, VariantBool variantBool) throws ComException
        {
            System.out.println("PowerPointEventHandler.windowBeforeRightClick");
        }

        public void windowBeforeDoubleClick(Selection selection, VariantBool variantBool) throws ComException
        {
            System.out.println("PowerPointEventHandler.windowBeforeDoubleClick");
        }

        public void presentationClose(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationClose");
        }

        public void presentationSave(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationSave");
        }

        public void presentationOpen(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationOpen");
        }

        public void newPresentation(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.newPresentation");
        }

        public void presentationNewSlide(_Slide slide) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationNewSlide");
        }

        public void windowActivate(_Presentation presentation, DocumentWindow documentWindow) throws ComException
        {
            System.out.println("PowerPointEventHandler.windowActivate");
        }

        public void windowDeactivate(_Presentation presentation, DocumentWindow documentWindow) throws ComException
        {
            System.out.println("PowerPointEventHandler.windowDeactivate");
        }

        public void slideShowBegin(SlideShowWindow slideShowWindow) throws ComException
        {
            System.out.println("PowerPointEventHandler.slideShowBegin");
        }

        public void slideShowNextBuild(SlideShowWindow slideShowWindow) throws ComException
        {
            System.out.println("PowerPointEventHandler.slideShowNextBuild");
        }

        public void slideShowNextSlide(SlideShowWindow slideShowWindow) throws ComException
        {
            System.out.println("PowerPointEventHandler.slideShowNextSlide");
        }

        public void slideShowEnd(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.slideShowEnd");
        }

        public void presentationPrint(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationPrint");
        }

        public void slideSelectionChanged(SlideRange slideRange) throws ComException
        {
            System.out.println("PowerPointEventHandler.slideSelectionChanged");
        }

        public void colorSchemeChanged(SlideRange slideRange) throws ComException
        {
            System.out.println("PowerPointEventHandler.colorSchemeChanged");
        }

        public void presentationBeforeSave(_Presentation presentation, VariantBool variantBool) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationBeforeSave");
        }

        public void slideShowNextClick(SlideShowWindow slideShowWindow, Effect effect) throws ComException
        {
            System.out.println("PowerPointEventHandler.slideShowNextClick");
        }

        public void afterNewPresentation(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.afterNewPresentation");
        }

        public void afterPresentationOpen(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventHandler.afterPresentationOpen");
        }

        public void presentationSync(_Presentation presentation, MsoSyncEventType msoSyncEventType) throws ComException
        {
            System.out.println("PowerPointEventHandler.presentationSync");
        }

        public void slideShowOnNext(SlideShowWindow slideShowWindow) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.slideShowOnNext");
        }

        public void slideShowOnPrevious(SlideShowWindow slideShowWindow) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.slideShowOnPrevious");
        }

        public void presentationBeforeClose(_Presentation presentation, VariantBool variantBool) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.presentationBeforeClose");
        }

        public void protectedViewWindowOpen(ProtectedViewWindow protectedViewWindow) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.protectedViewWindowOpen");
        }

        public void protectedViewWindowBeforeEdit(ProtectedViewWindow protectedViewWindow, VariantBool variantBool) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.protectedViewWindowBeforeEdit");
        }

        public void protectedViewWindowBeforeClose(ProtectedViewWindow protectedViewWindow, PpProtectedViewCloseReason ppProtectedViewCloseReason, VariantBool variantBool) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.protectedViewWindowBeforeClose");
        }

        public void protectedViewWindowActivate(ProtectedViewWindow protectedViewWindow) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.protectedViewWindowActivate");
        }

        public void protectedViewWindowDeactivate(ProtectedViewWindow protectedViewWindow) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.protectedViewWindowDeactivate");
        }

        public void presentationCloseFinal(_Presentation presentation) throws ComException
        {
            System.out.println("PowerPointEventsHandling$PowerPointEventHandler.presentationCloseFinal");
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                PowerPointEventsHandling powerPointEventsHandling = new PowerPointEventsHandling();
                powerPointEventsHandling.setSize(800, 600);
                powerPointEventsHandling.setLocationRelativeTo(null);
                powerPointEventsHandling.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                powerPointEventsHandling.setVisible(true);
            }
        });
    }
}
