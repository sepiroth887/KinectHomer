/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package comsample.comsamplelib;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import comsample.comsamplelib.impl.*;

/**
 * Represents Java interface for COM interface IComTest
 */
public interface IComTest extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{49D0CE9C-2A1B-4339-ABCF-C4C517C831F9}";

    BStr getString()
        throws ComException;

    Int getInteger()
        throws ComException;

    Variant getVariant()
        throws ComException;
}