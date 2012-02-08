/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * Use is subject to license terms.
 */

package com.teamdev.javacomserver;

import com.jniwrapper.ComplexArray;
import com.jniwrapper.UInt;
import com.jniwrapper.UInt16;
import com.jniwrapper.ULongInt;
import com.jniwrapper.win32.automation.ITypeInfo;
import com.jniwrapper.win32.automation.impl.IDispatchImpl;
import com.jniwrapper.win32.automation.types.DispID;
import com.jniwrapper.win32.automation.types.DispParams;
import com.jniwrapper.win32.automation.types.ExcepInfo;
import com.jniwrapper.win32.automation.types.Variant;
import com.jniwrapper.win32.com.ComException;
import com.jniwrapper.win32.com.IUnknown;
import com.jniwrapper.win32.com.types.IID;
import com.jniwrapper.win32.com.types.LocaleID;

public class ISample2Impl extends IDispatchImpl implements ISample2 {

    public ISample2Impl(IUnknown iUnknown) throws ComException {
        super(iUnknown);
    }

    public void sample2Method()
    {
    }
}
