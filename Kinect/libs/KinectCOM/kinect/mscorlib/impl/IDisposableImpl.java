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
 * Represents COM interface IDisposable.
 *
 *
 */
public class IDisposableImpl extends IDispatchImpl
    implements IDisposable
{
    public static final String INTERFACE_IDENTIFIER = IDisposable.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IDisposableImpl()
    {
    }

    protected IDisposableImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IDisposableImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IDisposableImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IDisposableImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void dispose()
        throws ComException
    {
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IDisposableImpl(this);
    }
}
