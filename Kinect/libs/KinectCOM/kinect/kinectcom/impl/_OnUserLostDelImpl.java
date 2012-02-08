package kinect.kinectcom.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.kinectcom.*;

/**
 * Represents COM interface _OnUserLostDel.
 *
 *
 */
public class _OnUserLostDelImpl extends IDispatchImpl
    implements _OnUserLostDel
{
    public static final String INTERFACE_IDENTIFIER = _OnUserLostDel.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _OnUserLostDelImpl()
    {
    }

    protected _OnUserLostDelImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _OnUserLostDelImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _OnUserLostDelImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _OnUserLostDelImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _OnUserLostDelImpl(this);
    }
}
