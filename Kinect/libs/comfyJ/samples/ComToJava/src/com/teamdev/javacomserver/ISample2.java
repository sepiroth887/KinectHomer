/*
 * Copyright (c) 2000-2011 TeamDev Ltd. All rights reserved.
 * Use is subject to license terms.
 */

package com.teamdev.javacomserver;

import com.jniwrapper.DoubleFloat;
import com.jniwrapper.Int;
import com.jniwrapper.UInt32;
import com.jniwrapper.win32.automation.IDispatch;
import com.jniwrapper.win32.automation.types.BStr;
import com.jniwrapper.win32.automation.types.SafeArray;
import com.jniwrapper.win32.automation.types.Variant;

public interface ISample2 extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{4D333CC0-BB04-4E05-9ED9-5DCA121DE8EE}";

    public static final int DISPID_sample2Method = 200;

    public void sample2Method();
}
