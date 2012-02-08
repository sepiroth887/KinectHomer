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
 * Represents COM interface _FileStream.
 *
 *
 */
public class _FileStreamImpl extends IDispatchImpl
    implements _FileStream
{
    public static final String INTERFACE_IDENTIFIER = _FileStream.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _FileStreamImpl()
    {
    }

    protected _FileStreamImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _FileStreamImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _FileStreamImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _FileStreamImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _FileStreamImpl(this);
    }
}
