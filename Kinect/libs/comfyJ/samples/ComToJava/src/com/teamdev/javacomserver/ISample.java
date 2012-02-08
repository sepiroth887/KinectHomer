/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * Use is subject to license terms.
 */

package com.teamdev.javacomserver;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int;
import com.jniwrapper.SingleFloat;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.SafeArray;
import com.jniwrapper.win32.automation.types.Variant;

public interface ISample extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{B478BAFA-FD74-4CDC-8487-5A8B450853AD}";

    public static final int DISPID_voidMethod = 100;
    public static final int DISPID_oneParamMethod = 101;
    public static final int DISPID_threeParamMethod = 102;
    public static final int DISPID_stringReturnMethod = 103;
    public static final int DISPID_intReturnMethodWithParam = 104;
    public static final int DISPID_getSample2 = 105;

    public void voidMethod();
    public void oneParamMethod(UInt32 param);
    public void threeParamMethod(UInt32 param1, BStr param2, DoubleFloat param3);
    public BStr stringReturnMethod();
    public Int intReturnMethodWithParam(BStr param);
    public ISample2 getSample2();
}