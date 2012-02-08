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
 * Represents COM interface _ManifestResourceInfo.
 *
 *
 */
public class _ManifestResourceInfoImpl extends IDispatchImpl
    implements _ManifestResourceInfo
{
    public static final String INTERFACE_IDENTIFIER = _ManifestResourceInfo.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _ManifestResourceInfoImpl()
    {
    }

    protected _ManifestResourceInfoImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _ManifestResourceInfoImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _ManifestResourceInfoImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _ManifestResourceInfoImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _ManifestResourceInfoImpl(this);
    }
}
