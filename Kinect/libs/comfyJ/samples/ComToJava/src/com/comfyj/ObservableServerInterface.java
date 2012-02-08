package com.comfyj;

import com.jniwrapper.Int32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;

public interface ObservableServerInterface extends IDispatch {
    public static final String INTERFACE_IDENTIFIER = "{F758B5BE-2DE2-44a2-8C5C-5726422F9018}";

    void fireEvent(Int32 intValue, BStr stringValue);
}
