package com.comfyj;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.ULongInt;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.server.IUnknownServer;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.IEnumConnectionPoints;
import com.jniwrapper.win32.ole.IEnumConnections;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import com.jniwrapper.win32.ole.impl.IConnectionPointImpl;
import com.jniwrapper.win32.ole.impl.IEnumConnectionPointsImpl;
import com.jniwrapper.win32.ole.server.IConnectionPointVTBL;
import com.jniwrapper.win32.ole.server.IEnumConnectionPointsVTBL;

import java.util.*;

/**
 * Java COM server that demonstrates how to implement IConnectionPointContainer interface and fire events from server.
 *
 * @author Serge Piletsky
 */
public class ObservableServer extends DispatchComServer implements IConnectionPointContainer, ObservableServerInterface {
    public static final CLSID COM_SERVER_CLSID = new CLSID("{FEEB200A-49EC-438d-8519-96643F29A5CF}");
    public static final String PROG_ID = "comfyj.myobservableserver.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "comfyj.myobservableserver";
    public static final String COM_SERVER_DESCRIPTION = "Java COM server that implements IConnectionPointContainer interface";

    private Map listeners = new HashMap();

    private IConnectionPointImpl connectionPoint;
    private final IID iid = new IID(ObservableServerEvents.INTERFACE_IDENTIFIER);

    public ObservableServer(CoClassMetaInfo classImpl) {
        super(classImpl);
        System.out.println("ObservableServer.ObservableServer");
    }

    public void fireEvent(final Int32 intValue, final BStr stringValue) {
        System.out.println("ObservableServer.fireEvent()");
        for (Iterator i = listeners.keySet().iterator(); i.hasNext();) {
            Integer cookie = (Integer) i.next();
            final IDispatch listener = (IDispatch) listeners.get(cookie);
            nofityListener(intValue, stringValue, listener);
        }
    }

    private void nofityListener(final Int32 intValue, final BStr stringValue, final IDispatch listener) {
        try {
            DispatchComServerFactory.getOleMessageLoop().doInvokeAndWait(new Runnable() {
                public void run() {
                    System.out.println("ObservableServer.notifyListener>onEvent");
                    Automation.invokeDispatch(listener, "onEvent", new Parameter[]{intValue, stringValue}, void.class);
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public IEnumConnectionPoints enumConnectionPoints() throws ComException {
        System.out.println("ObservableServer.enumConnectionPoints");


        List connectionPoints = new ArrayList(1);
        connectionPoints.add(getConnectionPoint());

        IEnumConnectionPointsImpl result = EnumConnectionPointsImpl.create(connectionPoints);
        result.setAutoDelete(false);

        return result;
    }

    public IConnectionPoint findConnectionPoint(IID riid) throws ComException {
        System.out.println("ObservableServer.findConnectionPoint, riid = " + riid);

        if (!iid.equals(riid)) {
            throw new ComException(HResult.CONNECT_E_ADVISELIMIT);
        }

        return getConnectionPoint();
    }

    private IConnectionPointImpl getConnectionPoint() {
        if (connectionPoint == null) {
            IConnectionPointContainerImpl thisContianer = new IConnectionPointContainerImpl();
            doQueryInterface(thisContianer.getIID(), thisContianer);
            connectionPoint = ConnectionPointImpl.create(listeners, thisContianer, iid);
        }
        return connectionPoint;
    }

    public ULongInt release() {
        System.out.println("ObservableServer.release, refCount= " + getRefCount());
        return super.release();
    }

    public static class EnumConnectionPointsImpl extends IUnknownServer implements IEnumConnectionPoints {
        static IClassFactoryServer classFactoryServer;
        static IClassFactory classFactory;

        static {
            classFactoryServer = new IClassFactoryServer(EnumConnectionPointsImpl.class);
            classFactoryServer.registerInterface(IEnumConnectionPoints.class, new IEnumConnectionPointsVTBL(classFactoryServer));
            classFactoryServer.setDefaultInterface(IEnumConnectionPoints.class);
            classFactory = classFactoryServer.createIClassFactory();
        }

        private static int index = 0;
        private List connectionPoints;

        public static IEnumConnectionPointsImpl create(List connectioPoints) {
            IEnumConnectionPointsImpl result = new IEnumConnectionPointsImpl();
            classFactory.createInstance(null, result.getIID(), result);

            EnumConnectionPointsImpl instance = (EnumConnectionPointsImpl) classFactoryServer.getInstances().pop();
            instance.setData(connectioPoints);

            return result;
        }

        public EnumConnectionPointsImpl(CoClassMetaInfo classImpl) {
            super(classImpl);
            System.out.println("ObservableServer$EnumConnectionPointsImpl.EnumConnectionPointsImpl");
        }

        public void setData(List connectionPoints) {
            this.connectionPoints = connectionPoints;
            System.out.println("ObservableServer$EnumConnectionPointsImpl.setData");
        }

        public Int32 next(Int32 cConnections, IConnectionPoint rgpcn, Int32 lpcFetched) {
            System.out.println("ObservableServer$EnumConnectionPointsImpl.next");
            if (cConnections.getValue() == 0 && cConnections.getValue() > 1) {
                throw new ComException(HResult.E_INVALIDARG);
            }

            if (index >= connectionPoints.size()) {
                return new HResult(HResult.S_FALSE);
            }

            IConnectionPointImpl connectionPoint = (IConnectionPointImpl) connectionPoints.get(index);
            connectionPoint.queryInterface(connectionPoint.getIID(), rgpcn);
            lpcFetched.setValue(1);

            index++;
            return new Int32(HResult.S_OK);
        }

        public void skip(Int32 cConnections) throws ComException {
            System.out.println("ObservableServer$EnumConnectionPointsImpl.skip");
            index += cConnections.getValue();
        }

        public void reset() throws ComException {
            System.out.println("ObservableServer$EnumConnectionPointsImpl.reset");
            index = 0;
        }

        public IEnumConnectionPoints invokeClone() throws ComException {
            System.out.println("ObservableServer$EnumConnectionPointsImpl.invokeClone");
            throw new ComException(HResult.E_NOTIMPL);
        }
    }

    public static class ConnectionPointImpl extends IUnknownServer implements IConnectionPoint {
        private static IClassFactoryServer classFactoryServer;
        private static IClassFactory classFactory;

        static {
            classFactoryServer = new IClassFactoryServer(ConnectionPointImpl.class);
            classFactoryServer.registerInterface(IConnectionPoint.class, new IConnectionPointVTBL(classFactoryServer));
            classFactoryServer.setDefaultInterface(IConnectionPoint.class);
            classFactory = classFactoryServer.createIClassFactory();
        }

        public static IConnectionPointImpl create(Map listeners, IConnectionPointContainer parentContainer, IID iid) {
            IConnectionPointImpl result = new IConnectionPointImpl();
            classFactory.createInstance(null, result.getIID(), result);

            ConnectionPointImpl connectionPoint = (ConnectionPointImpl) classFactoryServer.getInstances().pop();
            connectionPoint.setData(listeners, parentContainer, iid);

            return result;
        }

        private int lastCookie;
        private Map listeners;
        private IConnectionPointContainer parentContainer;
        private IID iid;

        public ConnectionPointImpl(CoClassMetaInfo classImpl) {
            super(classImpl);
            System.out.println("ObservableServer$ConnectionPointImpl.ConnectionPointImpl");
        }

        void setData(Map listeners, IConnectionPointContainer parentContainer, IID iid) {
            System.out.println("ObservableServer$ConnectionPointImpl.setData");
            this.listeners = listeners;
            this.parentContainer = parentContainer;
            this.iid = iid;
        }

        public void getConnectionInterface(IID piid) throws ComException {
            System.out.println("ObservableServer$ConnectionPointImpl.getConnectionInterface");
            piid.initFrom(iid);
        }

        public IConnectionPointContainer getConnectionPointContainer() throws ComException {
            System.out.println("ObservableServer$ConnectionPointImpl.getConnectionPointContainer");
            return parentContainer;
        }

        public Int32 advise(IUnknown listener) throws ComException {
            System.out.println("ObservableServer$ConnectionPointImpl.advise");
            Integer key = new Integer(lastCookie++);

            listeners.put(key, new IDispatchImpl(listener));
            return new Int32(key.intValue());
        }

        public void unadvise(Int32 dwCookie) throws ComException {
            System.out.println("ObservableServer$ConnectionPointImpl.unadvise");
            Integer cookie = new Integer((int) dwCookie.getValue());
            IDispatch listener = (IDispatch) listeners.get(cookie);
            if (listener != null) {
                listener.setAutoDelete(false);
                listener.release();
            }
            listeners.remove(cookie);
        }

        public IEnumConnections enumConnections() throws ComException {
            System.out.println("ObservableServer$ConnectionPointImpl.enumConnections");
            throw new ComException(HResult.E_NOTIMPL);
        }
    }
}
