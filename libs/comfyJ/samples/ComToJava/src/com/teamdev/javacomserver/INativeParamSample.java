/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * Use is subject to license terms.
 */

package com.teamdev.javacomserver;

import com.jniwrapper.win32.automation.IDispatch;

public interface INativeParamSample extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{A0CE0942-BC80-4AF0-A4C8-C26B39489413}";

    public static final int DISPID_nativeMethod1 = 301;
    public static final int DISPID_nativeMethod2 = 302;

    void nativeMethod1(int param);
    void nativeMethod2(String param1, String param2);
}
