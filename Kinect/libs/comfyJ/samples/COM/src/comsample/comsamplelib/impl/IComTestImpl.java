/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package comsample.comsamplelib.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import comsample.comsamplelib.*;

/**
 * Represents COM interface IComTest
 */
public class IComTestImpl extends IDispatchImpl
    implements IComTest
{
    public static final String INTERFACE_IDENTIFIER = IComTest.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IComTestImpl()
    {
    }

    protected IComTestImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IComTestImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IComTestImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IComTestImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public BStr getString()
        throws ComException
    {
        BStr result = new BStr();
        invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                result == null ? (Parameter)PTR_NULL : new Pointer.OutOnly(result)
            }
        );
        return result;
    }

    public Int getInteger()
        throws ComException
    {
        Int result = new Int();
        invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                result == null ? (Parameter)PTR_NULL : new Pointer.OutOnly(result)
            }
        );
        return result;
    }

    public Variant getVariant()
        throws ComException
    {
        Variant result = new Variant();
        invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                result == null ? (Parameter)PTR_NULL : new Pointer(result)
            }
        );
        return result;
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IComTestImpl(this);
    }
}