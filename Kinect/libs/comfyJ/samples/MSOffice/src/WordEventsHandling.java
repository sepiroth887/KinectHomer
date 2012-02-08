import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleContainer;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import word.word.ApplicationEvents2;
import word.word.Selection;
import word.word._Application;
import word.word._Document;
import word.word.impl.ApplicationEvents2Impl;
import word.word.impl._DocumentImpl;
import word.word.server.ApplicationEvents2Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WordEventsHandling extends JFrame {
    private IClassFactoryServer classFactoryServer;
    private OleContainer container;

    private _Application application;
    private IConnectionPoint connectionPoint;
    private Int32 eventsHandlerIdentifier;

    public WordEventsHandling() {
        super("Word events handling sample");

        container = new OleContainer();
        container.createObject("Word.Document");

        getContentPane().add(container, BorderLayout.CENTER);
        installEventsHandler();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("windowClosing");
                release();
            }
        });
    }

    private void release() {
        try {
            container.getOleMessageLoop().doInvokeAndWait(new Runnable() {
                public void run() {
                    connectionPoint.unadvise(eventsHandlerIdentifier);
                    connectionPoint.setAutoDelete(false);
                    connectionPoint.release();
                    container.destroyObject();
                    application.setAutoDelete(false);
                    application.release();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void installEventsHandler() {
        try {
            container.getOleMessageLoop().doInvokeAndWait(new Runnable() {
                public void run() {
                    _Document workbook = new _DocumentImpl(container.getOleObject());

                    classFactoryServer = new IClassFactoryServer(AppEventsHandler.class);
                    classFactoryServer.registerInterface(ApplicationEvents2.class, new IDispatchVTBL(classFactoryServer));
                    classFactoryServer.setDefaultInterface(IDispatch.class);

                    IClassFactory classFactory = classFactoryServer.createIClassFactory();
                    ApplicationEvents2Impl appEventsImpl = new ApplicationEvents2Impl();
                    classFactory.createInstance(null, appEventsImpl.getIID(), appEventsImpl);

                    application = workbook.getApplication();

                    IConnectionPointContainer cpc = new IConnectionPointContainerImpl(application);

                    connectionPoint = cpc.findConnectionPoint(new IID(ApplicationEvents2.INTERFACE_IDENTIFIER));

                    eventsHandlerIdentifier = connectionPoint.advise(appEventsImpl);

                    classFactory.setAutoDelete(false);

                    workbook.setAutoDelete(false);
                    workbook.release();

                    cpc.setAutoDelete(false);
                    cpc.release();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class AppEventsHandler extends ApplicationEvents2Server {
        public AppEventsHandler(CoClassMetaInfo classImpl) {
            super(classImpl);
            registerMethods(ApplicationEvents2.class, false);
            System.out.println("AppEventsHandler.<init>");
        }

        public void startup() {
            System.out.println("WordEventsHandling$AppEventsHandler.startup");
        }

        public void quit() {
            System.out.println("WordEventsHandling$AppEventsHandler.quit");
        }

        public void documentChange() {
            System.out.println("WordEventsHandling$AppEventsHandler.documentChange");
        }

        public void documentOpen(_Document Doc) {
            System.out.println("WordEventsHandling$AppEventsHandler.documentOpen");
        }

        public void documentBeforeClose(_Document Doc, VariantBool Cancel) {
            System.out.println("WordEventsHandling$AppEventsHandler.documentBeforeClose");
        }

        public void documentBeforePrint(_Document Doc, VariantBool Cancel) {
            System.out.println("WordEventsHandling$AppEventsHandler.documentBeforePrint");
        }

        public void documentBeforeSave(_Document Doc, VariantBool SaveAsUI, VariantBool Cancel) {
            System.out.println("WordEventsHandling$AppEventsHandler.documentBeforeSave");
        }

        public void newDocument(_Document Doc) {
            System.out.println("WordEventsHandling$AppEventsHandler.newDocument");
        }

        public void windowActivate(_Document Doc, Window Wn) {
            System.out.println("WordEventsHandling$AppEventsHandler.windowActivate");
        }

        public void windowDeactivate(_Document Doc, Window Wn) {
            System.out.println("WordEventsHandling$AppEventsHandler.windowDeactivate");
        }

        public void windowSelectionChange(Selection Sel) {
            System.out.println("WordEventsHandling$AppEventsHandler.windowSelectionChange");
        }

        public void windowBeforeRightClick(Selection Sel, VariantBool Cancel) {
            System.out.println("WordEventsHandling$AppEventsHandler.windowBeforeRightClick");
        }

        public void windowBeforeDoubleClick(Selection Sel, VariantBool Cancel) {
            System.out.println("WordEventsHandling$AppEventsHandler.windowBeforeDoubleClick");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WordEventsHandling powerPointEventsHandling = new WordEventsHandling();
                powerPointEventsHandling.setSize(800, 600);
                powerPointEventsHandling.setLocationRelativeTo(null);
                powerPointEventsHandling.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                powerPointEventsHandling.setVisible(true);
            }
        });
    }
}
