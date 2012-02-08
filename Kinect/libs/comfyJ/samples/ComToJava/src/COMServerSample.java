/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.Int32;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.server.IDispatchServer;
import com.jniwrapper.win32.com.server.IUnknownServer;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.com.types.RegCls;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.IEnumConnectionPoints;
import com.jniwrapper.win32.ole.IEnumConnections;
import com.jniwrapper.win32.ole.impl.IConnectionPointImpl;
import com.jniwrapper.win32.ole.server.IConnectionPointContainerVTBL;
import com.jniwrapper.win32.ole.server.IConnectionPointVTBL;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This sample demonstrates how to create and register Java COM Server dynamically at runtime.
 *
 * @author Serge Piletsky
 */
public class COMServerSample
{
    public static interface CustomInterface extends IDispatch
    {
        public static final String INTERFACE_IDENTIFIER = "{4b4ea97c-ae41-4b4b-97d4-000000000002}";

        int DISPID_method1 = 100;
        int DISPID_method2 = 101;

        void method1(BStr value1, Int32 value2);

        void method2(Int32 value);
    }

    /**
     * The sample Java COM class, which implements standard {@link IPersistFile} interface.
     */
    public static class COMServer extends IDispatchServer
//            implements IPersistFile, IConnectionPointContainer
            implements CustomInterface, IConnectionPointContainer
    {
        // Unique CLSID for our server class
        public static final CLSID CLSID = new CLSID("{4b4ea97c-ae41-4b4b-97d4-34b3bf71c630}");

        private IConnectionPointImpl _connectionPoint;
        private OurConnectionPoint _ourConnectionPoint;

        private Timer _notificationTimer;

        public COMServer(CoClassMetaInfo classImpl)
        {
            super(classImpl);
            System.out.println("COMServer.<init>");

            // produce COM events every second
            _notificationTimer = new Timer(1000, new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (_ourConnectionPoint != null)
                    {
                        OleMessageLoop.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                _ourConnectionPoint.fireEvent();
                            }
                        });
                    }
                }
            });
            _notificationTimer.start();
        }

        public void method1(BStr value1, Int32 value2)
        {
            System.out.println("COMServer.method1: value1 = " + value1 + ", value2 = " + value2);
        }

        public void method2(Int32 value)
        {
            System.out.println("COMServer.method2: value = " + value);
        }

//        public Int32 isDirty()
//        {
//            System.out.println("COMServer.isDirty:");
//            final Int32 result = new Int32(1);
//            System.out.println("\tresult = " + result);
//            return result;
//        }
//
//        public void load(OleStr pszFileName, StgMode dwMode) throws ComException
//        {
//            System.out.println("COMServer.load:");
//            System.out.println("\tpszFileName = " + pszFileName);
//            System.out.println("\tdwMode = " + dwMode);
//        }
//
//        public void save(OleStr pszFileName, VariantBool fRemember) throws ComException
//        {
//            System.out.println("COMServer.save:");
//            System.out.println("\tpszFileName = " + pszFileName);
//            System.out.println("\tfRemember = " + fRemember);
//        }
//
//        public void saveCompleted(OleStr pszFileName) throws ComException
//        {
//            System.out.println("COMServer.saveCompleted:");
//            System.out.println("\tpszFileName = " + pszFileName);
//        }
//
//        public OleStr getCurFile() throws ComException
//        {
//            System.out.println("COMServer.getCurFile:");
//            final OleStr result = new OleStr("C:\\Test.txt");
//            System.out.println("\tresult = " + result);
//            return result;
//        }
//
//        public void getClassID(CLSID pClassID) throws ComException
//        {
//            System.out.println("COMServer.getClassID:");
//            System.out.println("\tpClassID = " + pClassID);
//        }

        public IEnumConnectionPoints enumConnectionPoints() throws ComException
        {
            System.out.println("COMServer.enumConnectionPoints");
            throw new ComException(HResult.E_NOTIMPL);
        }

        public IConnectionPoint findConnectionPoint(IID riid) throws ComException
        {
            System.out.println("COMServer.findConnectionPoint, riid = " + riid);

            if (_connectionPoint == null)
            {
                ConnectionPointFactory connectionPointFactory = new ConnectionPointFactory();
                IClassFactory classFactory = connectionPointFactory.createIClassFactory();

                IUnknownImpl connectionPoint = new IUnknownImpl();
                classFactory.createInstance(null, connectionPoint.getIID(), connectionPoint);
                _connectionPoint = new IConnectionPointImpl(connectionPoint);
                _ourConnectionPoint = (OurConnectionPoint) connectionPointFactory.getInstances().pop();
            }
            return _connectionPoint;
        }
    }

    public static class OurConnectionPoint extends IUnknownServer implements IConnectionPoint
    {
        private Map _listeners = new HashMap();
        private int _listenerIndex = 0;

        public OurConnectionPoint(CoClassMetaInfo classImpl)
        {
            super(classImpl);
        }

        public void getConnectionInterface(IID piid) throws ComException
        {
            System.out.println("OurConnectionPoint.getConnectionInterface, piid = " + piid);
            throw new ComException(HResult.E_NOTIMPL);
        }

        public IConnectionPointContainer getConnectionPointContainer() throws ComException
        {
            System.out.println("OurConnectionPoint.getConnectionPointContainer()");
            throw new ComException(HResult.E_NOTIMPL);
        }

        public Int32 advise(IUnknown pObjetPtr) throws ComException
        {
            System.out.println("OurConnectionPoint.advise, pObjetPtr = " + pObjetPtr);
            Integer key = new Integer(_listenerIndex++);

            IDispatchImpl listener = new IDispatchImpl(pObjetPtr);
            _listeners.put(key, listener);
            return new Int32(key.intValue());
        }

        public void unadvise(Int32 dwCookie) throws ComException
        {
            System.out.println("OurConnectionPoint.unadvise");
            _listeners.remove(new Integer((int) dwCookie.getValue()));
        }

        public IEnumConnections enumConnections() throws ComException
        {
            System.out.println("OurConnectionPoint.enumConnections");
            throw new ComException(HResult.E_NOTIMPL);
        }

        void fireEvent()
        {
            System.out.println("OurConnectionPoint.fireEvent()");
            for (Iterator i = _listeners.keySet().iterator(); i.hasNext();)
            {
                Integer key = (Integer) i.next();
                IDispatchImpl listener = (IDispatchImpl) _listeners.get(key);
                if (listener != null)
                {
                    try
                    {
                        System.out.print("notifying the listener... ");

                        Automation automation = new Automation(listener, true);
                        automation.invoke("DataReady", new Object[]{"Message!"});

                        System.out.println("done.");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    System.err.println("\tListener is NULL!");
                }
            }
        }
    }

    static class ConnectionPointFactory extends IClassFactoryServer
    {
        public ConnectionPointFactory()
        {
            super(OurConnectionPoint.class);
            registerInterface(IConnectionPoint.class, new IConnectionPointVTBL(this));
            setDefaultInterface(IConnectionPoint.class);
        }
    }

    public static interface COMServerEvents extends IDispatch
    {
        public static final String INTERFACE_IDENTIFIER = "{4b4ea97c-ae41-4b4b-97d4-000000000001}";

        int DISPID_DataReady = 100;

        void DataReady(BStr str);
    }

    /**
     * Our COMServerClassFactory
     */
    static class OurCOMServerClassFactory extends IClassFactoryServer
    {
        public OurCOMServerClassFactory()
        {
            super(COMServer.class);
            // Register all implemented interfaces
            registerInterface(IDispatch.class, new IDispatchVTBL(this));
            registerInterface(CustomInterface.class, new IDispatchVTBL(this));
//            registerInterface(IPersistFile.class, new IPersistFileVTBL(this));
            registerInterface(IConnectionPointContainer.class, new IConnectionPointContainerVTBL(this));
//            setDefaultInterface(IPersistFile.class);
            setDefaultInterface(IDispatch.class);
        }

        public void createInstance(IUnknown outer, IID iid, IUnknown result) throws ComException
        {
            System.out.println("OurCOMServerClassFactory.createInstance, outer = " + outer.isNull() + ", iid = " + iid + "; result = " + result);


            super.createInstance(outer, iid, result);
        }
    }

    public static void main(String[] args)
    {
        final long[] tokenValue = new long[1];

        // The registration of our IClassFactory should be performed in the OleMessageLoop class
        try
        {
            OleMessageLoop.invokeAndWait(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        // Create the OurCOMServerClassFactory instance for our COMServer class
                        OurCOMServerClassFactory ourClassFactoryServer = new OurCOMServerClassFactory();

                        // Create the new IClassFactory instance.
                        // This factory will be used by consumers of this factory to produce instances of our COMServer class.
                        IClassFactory ourClassFactory = ourClassFactoryServer.createIClassFactory();

                        System.out.println("Registering our IClassFactory instance...");
                        // Register our instance of class factory in the internal table of the registered class factories
                        final UInt32 token = ComFunctions.coRegisterClassObject(COMServer.CLSID,
                                ourClassFactory,
                                ClsCtx.LOCAL_SERVER,
                                new RegCls(RegCls.REGCLS_MULTIPLEUSE));

                        tokenValue[0] = token.getValue();
                        System.out.println("Our IClassFactory instance is successfully registered.");
                    }
                    catch (ComException e)
                    {
                        System.err.println("Failed to register our IClassFactory instance because of error: " + e);
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Press 'Enter' to terminate.\n");
        // Run until Enter key is pressed
        try
        {
            System.in.read();
            System.out.println("Revoking our IClassFactory instance...");
            try
            {
                OleMessageLoop.invokeAndWait(new Runnable()
                {
                    public void run()
                    {
                        // Revoke our class factory from the internal table of registered class factories
                        ComFunctions.coRevokeClassObject(new UInt32(tokenValue[0]));
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            OleMessageLoop.stop();
            System.out.println("Our IClassFactory instance is successfully revoked.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}