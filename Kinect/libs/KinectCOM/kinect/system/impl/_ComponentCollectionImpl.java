package kinect.system.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.system.*;

/**
 * Represents COM interface _ComponentCollection.
 *
 *
 */
public class _ComponentCollectionImpl extends IDispatchImpl
    implements _ComponentCollection
{
    public static final String INTERFACE_IDENTIFIER = _ComponentCollection.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _ComponentCollectionImpl()
    {
    }

    protected _ComponentCollectionImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _ComponentCollectionImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _ComponentCollectionImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _ComponentCollectionImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _ComponentCollectionImpl(this);
    }
}
