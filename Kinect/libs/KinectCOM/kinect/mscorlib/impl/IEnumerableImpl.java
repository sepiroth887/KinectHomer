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
import kinect.stdole.*;
import kinect.stdole.impl.*;

/**
 * Represents COM interface IEnumerable.
 *
 *
 */
public class IEnumerableImpl extends IDispatchImpl
    implements IEnumerable
{
    public static final String INTERFACE_IDENTIFIER = IEnumerable.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IEnumerableImpl()
    {
    }

    protected IEnumerableImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IEnumerableImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IEnumerableImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IEnumerableImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public IEnumVARIANT getEnumerator()
        throws ComException
    {
        IEnumVARIANTImpl pRetVal = new IEnumVARIANTImpl();
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IEnumerableImpl(this);
    }
}
