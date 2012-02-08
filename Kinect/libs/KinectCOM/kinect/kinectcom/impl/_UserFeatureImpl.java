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
 * Represents COM interface _UserFeature.
 *
 *
 */
public class _UserFeatureImpl extends IDispatchImpl
    implements _UserFeature
{
    public static final String INTERFACE_IDENTIFIER = _UserFeature.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _UserFeatureImpl()
    {
    }

    protected _UserFeatureImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _UserFeatureImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _UserFeatureImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _UserFeatureImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _UserFeatureImpl(this);
    }
}
