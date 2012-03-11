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
 * Represents COM interface _Confidence.
 *
 *
 */
public class _ConfidenceImpl extends IDispatchImpl
    implements _Confidence
{
    public static final String INTERFACE_IDENTIFIER = _Confidence.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _ConfidenceImpl()
    {
    }

    protected _ConfidenceImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _ConfidenceImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _ConfidenceImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _ConfidenceImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _ConfidenceImpl(this);
    }
}
