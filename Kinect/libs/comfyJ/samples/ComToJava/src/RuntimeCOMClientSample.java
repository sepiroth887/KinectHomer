/*
 * Copyright (c) 2002-2011 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.jniwrapper.com/pages/comfyj/license
 */
import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.OleMessageLoop;
import com.jniwrapper.win32.automation.OleStr;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.VariantBool;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.IPersistFile;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IPersistFileImpl;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.server.IDispatchServer;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import com.jniwrapper.win32.stg.types.StgMode;

/**
 * This sample demonstrates how to create an instance of our {@link RuntimeCOMServerSample.COMServer} class.
 * <p/>
 * <b>NOTE:</b>The {@link RuntimeCOMServerSample} application should be started first.
 *
 * @author Serge Piletsky
 */
public class RuntimeCOMClientSample
{
    public static class COMServerEventsImpl extends IDispatchServer implements RuntimeCOMServerSample.COMServerEvents
    {
        public COMServerEventsImpl(CoClassMetaInfo classImpl)
        {
            super(classImpl);
            registerMethods(RuntimeCOMServerSample.COMServerEvents.class);
        }

        public void DataReady(BStr str)
        {
            System.out.println("COMServerEventsImpl.DataReady, str = " + str);
        }
    }


    public static void main(String[] args)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                System.out.println("Creating the instance of our COMServer class...");
//                IUnknown unknown = new IPersistFileImpl(COMServerSample.COMServer.CLSID, ClsCtx.LOCAL_SERVER);
                IUnknown unknown = new IUnknownImpl(RuntimeCOMServerSample.COMServer.CLSID, ClsCtx.LOCAL_SERVER);
                System.out.println("Instance of our COMServer class is created successfully.");

                IClassFactoryServer server = new IClassFactoryServer(COMServerEventsImpl.class);
                server.registerInterface(IDispatch.class, new IDispatchVTBL(server));
                server.setDefaultInterface(IDispatch.class);

                IClassFactory classFactory = server.createIClassFactory();
                IDispatchImpl handler = new IDispatchImpl();
                classFactory.createInstance(null, handler.getIID(), handler);

                IConnectionPointContainer connectionPointContainer = new IConnectionPointContainerImpl(unknown);
                IConnectionPoint connectionPoint = connectionPointContainer.findConnectionPoint(new IID(RuntimeCOMServerSample.COMServerEvents.INTERFACE_IDENTIFIER));
                System.out.println("Advising, connectionPoint = " + connectionPoint.isNull());
                connectionPoint.advise(handler);
                System.out.println("Advised.");

                System.out.println("Calling the various methods of our COMServer instance...");

                // There should be appropriate output in the Java console of the server application
                IPersistFile persistFile = new IPersistFileImpl(unknown);
                final OleStr curFile = persistFile.getCurFile();
                System.out.println("curFile = " + curFile);
                final Int32 dirty = persistFile.isDirty();
                System.out.println("dirty = " + dirty);
                persistFile.load(new OleStr("Test.class"), new StgMode(StgMode.STGM_CREATE));
                persistFile.save(new OleStr("Test.java"), new VariantBool(true));
                // After the calling of this method, we should receive notification in our listener instance.
                persistFile.saveCompleted(new OleStr("Test2.java"));

                // call methods of the com object using Automation
                Automation automation = new Automation(unknown);
                automation.invoke("method1", new Object[] {"string parameter1", new Integer(2)});
                automation.invoke("method2", new Object[] {new Integer(123)});
            }
        };
        try
        {
            OleMessageLoop.invokeAndWait(runnable);
            System.in.read();
            OleMessageLoop.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
