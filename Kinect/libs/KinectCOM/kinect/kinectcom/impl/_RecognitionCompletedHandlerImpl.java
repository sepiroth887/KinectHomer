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
 * Represents COM interface _RecognitionCompletedHandler.
 *
 *
 */
public class _RecognitionCompletedHandlerImpl extends IDispatchImpl
    implements _RecognitionCompletedHandler
{
    public static final String INTERFACE_IDENTIFIER = _RecognitionCompletedHandler.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _RecognitionCompletedHandlerImpl()
    {
    }

    protected _RecognitionCompletedHandlerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _RecognitionCompletedHandlerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _RecognitionCompletedHandlerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _RecognitionCompletedHandlerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _RecognitionCompletedHandlerImpl(this);
    }
}
