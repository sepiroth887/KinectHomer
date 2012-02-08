/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.Int;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.DispatchComServer;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This Java COM server implements our custom {@link MyObservable} dispinterface.
 *
 * @author Serge Piletsky
 */
public class MyObservableJavaComObject extends DispatchComServer implements MyObservable {

    public static final CLSID COM_SERVER_CLSID = new CLSID("{A35B432E-5274-4146-9858-638313EDCEAA}");
    public static final String PROG_ID = "comfyj.observable.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "comfyj.observable";
    public static final String COM_SERVER_DESCRIPTION = "Observable JavaComServer";

    private List _observers = new LinkedList();

    public MyObservableJavaComObject(CoClassMetaInfo coClassMetaInfo) {
        super(coClassMetaInfo);
    }

    public void addObserver(IDispatch observer) {
        System.out.println("MyObservableJavaComObject.addObserver,  observer = " + observer);
        observer.addRef();
        _observers.add(observer);
    }

    public void notifyObservers(Variant value) {
        System.out.println("MyObservableJavaComObject.notifyObservers,  value= " + value.getValue());
        for (Iterator i = _observers.iterator(); i.hasNext();) {
            IUnknown observer = (IUnknown) i.next();
            try {
                Automation automation = new Automation(observer, true);
                automation.invoke("update", new Variant[]{value});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Int countObservers() {
        System.out.println("MyObservableJavaComObject.countObservers");
        return new Int(_observers.size());
    }

    public void simulateEventsFromJava() {
        System.out.println("MyObservableJavaComObject.simulateEventsFromJava");
        notifyObservers(new Variant("This is a string from Java Com"));
    }
}