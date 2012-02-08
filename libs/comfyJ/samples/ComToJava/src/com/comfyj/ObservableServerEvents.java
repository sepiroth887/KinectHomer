package com.comfyj;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;

public interface ObservableServerEvents extends IDispatch {
    public static final String INTERFACE_IDENTIFIER = "{AA206ADD-0D4D-4cf2-8921-30F1784BE8DC}";

    public void onEvent(Int32 intValue, BStr stringValue);
}
