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
 * Represents COM interface _OnGestureRecognitionCompletedDel.
 *
 *
 */
public class _OnGestureRecognitionCompletedDelImpl extends IDispatchImpl
    implements _OnGestureRecognitionCompletedDel
{
    public static final String INTERFACE_IDENTIFIER = _OnGestureRecognitionCompletedDel.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _OnGestureRecognitionCompletedDelImpl()
    {
    }

    protected _OnGestureRecognitionCompletedDelImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _OnGestureRecognitionCompletedDelImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _OnGestureRecognitionCompletedDelImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _OnGestureRecognitionCompletedDelImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _OnGestureRecognitionCompletedDelImpl(this);
    }
}
