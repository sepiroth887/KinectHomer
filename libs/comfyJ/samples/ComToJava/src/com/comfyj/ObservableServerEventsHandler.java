package com.comfyj;

import com.jniwrapper.win32.automation.server.IDispatchVTBL;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.IClassFactory;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.server.IClassFactoryServer;
import com.jniwrapper.win32.com.server.IDispatchServer;
import com.jniwrapper.Int32;

public class ObservableServerEventsHandler extends IDispatchServer implements ObservableServerEvents {

    private static IClassFactoryServer classFactoryServer;
    private static IClassFactory classFactory;

    public ObservableServerEventsHandler(CoClassMetaInfo coClassMetaInfo) {
        super(coClassMetaInfo);
        setDispInterface(true);
    }

    public void onEvent(Int32 intValue, BStr stringValue) {
        System.out.println("ObservableServerEventsHandler.onEvent, value = " + intValue + "; stringValue = " + stringValue);
    }

    public static ObservableServerEvents create() {
        if (classFactoryServer == null) {
            classFactoryServer = new IClassFactoryServer(ObservableServerEventsHandler.class);
            classFactoryServer.registerInterface(ObservableServerEvents.class, new IDispatchVTBL(classFactoryServer));
            classFactory = classFactoryServer.createIClassFactory();
        }

        ObservableServerEventsImpl handler = new ObservableServerEventsImpl();
        classFactory.createInstance(null, handler.getIID(), handler);

        return handler;
    }
}
