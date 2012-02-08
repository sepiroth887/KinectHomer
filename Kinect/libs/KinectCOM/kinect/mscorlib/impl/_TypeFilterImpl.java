package kinect.mscorlib.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.*;

/**
 * Represents COM interface _TypeFilter.
 *
 *
 */
public class _TypeFilterImpl extends IDispatchImpl
    implements _TypeFilter
{
    public static final String INTERFACE_IDENTIFIER = _TypeFilter.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _TypeFilterImpl()
    {
    }

    protected _TypeFilterImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _TypeFilterImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _TypeFilterImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _TypeFilterImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _TypeFilterImpl(this);
    }
}
