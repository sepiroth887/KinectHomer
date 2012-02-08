package com.comfyj;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.HResult;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.com.ComFunctions;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.impl.IUnknownImpl;
import com.jniwrapper.win32.com.types.ClsCtx;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.ole.IConnectionPoint;
import com.jniwrapper.win32.ole.IConnectionPointContainer;
import com.jniwrapper.win32.ole.IEnumConnectionPoints;
import com.jniwrapper.win32.ole.impl.IConnectionPointContainerImpl;
import com.jniwrapper.win32.ole.impl.IConnectionPointImpl;

/**
 * ObservableServer connects to the registered ObservableServer Java COM server, invokes its methods and receives evetns from it.
 */
public class ObservableClient {
    public static void main(String[] args) throws Exception {
        ComFunctions.coInitialize();

        IUnknown server = new IUnknownImpl(ObservableServer.COM_SERVER_CLSID, ClsCtx.LOCAL_SERVER);

        IConnectionPointContainer connectionPointContainer = new IConnectionPointContainerImpl(server);

        IEnumConnectionPoints connectionPoints = connectionPointContainer.enumConnectionPoints();
        IConnectionPoint connectionPoint;
//        connectionPoint = connectionPointContainer.findConnectionPoint(new IID(ObservableServerEvents.INTERFACE_IDENTIFIER));
        // OR
        while (true) {
            connectionPoint = new IConnectionPointImpl();
            Int32 fetched = new Int32();
            Int32 result = connectionPoints.next(new Int32(1), connectionPoint, fetched);

            if (fetched.getValue() == 0 || result.getValue() == HResult.S_FALSE) {
                break;
            }

            if (!connectionPoint.isNull()) {
                IID iid = new IID();
                connectionPoint.getConnectionInterface(iid);
                if (iid.equals(new IID(ObservableServerEvents.INTERFACE_IDENTIFIER))) {
                    break;
                }
            }
        }

        ObservableServerEvents observableServerEvents = ObservableServerEventsHandler.create();
        Int32 cookie = connectionPoint.advise(observableServerEvents);

        Automation serverAutomation = new Automation(server, true);
        serverAutomation.invoke("fireEvent", new Integer(100), "One hundred");
        serverAutomation.invoke("fireEvent", new Integer(500), "Five hundreds");
        serverAutomation.invoke("fireEvent", new Integer(1000), "One thousand");
        serverAutomation.release();

        connectionPoint.unadvise(cookie);

        connectionPointContainer.setAutoDelete(false);
        connectionPointContainer.release();

        connectionPoint.setAutoDelete(false);
        connectionPoint.release();

        server.setAutoDelete(false);
        server.release();
    }

}
