package com.comfyj;

import com.jniwrapper.Int32;
import com.jniwrapper.Parameter;
import com.jniwrapper.win32.automation.Automation;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.IID;

public class ObservableServerEventsImpl extends IDispatchImpl implements ObservableServerEvents {
    public static final String INTERFACE_IDENTIFIER = ObservableServerEvents.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public ObservableServerEventsImpl() {
    }

    public ObservableServerEventsImpl(IUnknown iUnknown) throws ComException {
        super(iUnknown);
    }

    public void onEvent(Int32 intValue, BStr stringValue) {
        Automation.invokeDispatch(this, "onEvent", new Parameter[]{intValue, stringValue}, void.class);
    }

    public IID getIID() {
        return _iid;
    }

    public Object clone() {
        return new ObservableServerEventsImpl(this);
    }
}
