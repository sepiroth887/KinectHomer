/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package comsample.comsamplelib;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import comsample.comsamplelib.impl.*;

/**
 * Represents COM coclass ComTest
 */
public class ComTest extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{FBB80E77-CCDA-4338-9B0E-11504CC16F82}");

    public ComTest()
    {
    }

    public ComTest(ComTest that)
    {
        super(that);
    }

    /**
     * creates coclass and returns its default interface
     */
    public static IComTest create(ClsCtx dwClsContext) throws ComException
    {
        final IComTestImpl intf = new IComTestImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * queries the <code>IComTest</code> interface from IUnknown instance
     */
    public static IComTest queryInterface(IUnknown unknown) throws ComException
    {
        final IComTestImpl result = new IComTestImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new ComTest(this);
    }
}