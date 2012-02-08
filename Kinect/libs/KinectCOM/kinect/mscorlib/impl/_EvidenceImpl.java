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
 * Represents COM interface _Evidence.
 *
 *
 */
public class _EvidenceImpl extends IDispatchImpl
    implements _Evidence
{
    public static final String INTERFACE_IDENTIFIER = _Evidence.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _EvidenceImpl()
    {
    }

    protected _EvidenceImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _EvidenceImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _EvidenceImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _EvidenceImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _EvidenceImpl(this);
    }
}
