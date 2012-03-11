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
 * Represents COM interface _EventHandler.
 *
 *
 */
public class _EventHandlerImpl extends IDispatchImpl
    implements _EventHandler
{
    public static final String INTERFACE_IDENTIFIER = _EventHandler.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _EventHandlerImpl()
    {
    }

    protected _EventHandlerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _EventHandlerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _EventHandlerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _EventHandlerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _EventHandlerImpl(this);
    }
}
