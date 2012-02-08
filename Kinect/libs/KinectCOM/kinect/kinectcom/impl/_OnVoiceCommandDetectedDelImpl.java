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
 * Represents COM interface _OnVoiceCommandDetectedDel.
 *
 *
 */
public class _OnVoiceCommandDetectedDelImpl extends IDispatchImpl
    implements _OnVoiceCommandDetectedDel
{
    public static final String INTERFACE_IDENTIFIER = _OnVoiceCommandDetectedDel.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _OnVoiceCommandDetectedDelImpl()
    {
    }

    protected _OnVoiceCommandDetectedDelImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _OnVoiceCommandDetectedDelImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _OnVoiceCommandDetectedDelImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _OnVoiceCommandDetectedDelImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _OnVoiceCommandDetectedDelImpl(this);
    }
}
