package kinect.stdole.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import kinect.stdole.*;

/**
 * Represents COM interface IEnumVARIANT.
 *
 *
 */
public class IEnumVARIANTImpl extends IUnknownImpl
    implements IEnumVARIANT
{
    public static final String INTERFACE_IDENTIFIER = IEnumVARIANT.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IEnumVARIANTImpl()
    {
    }

    protected IEnumVARIANTImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IEnumVARIANTImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IEnumVARIANTImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IEnumVARIANTImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void next(
        UInt32 /*[in]*/ celt,
        Variant /*[in]*/ rgvar,
        UInt32 /*[out]*/ pceltFetched)
        throws ComException
    {
       invokeStandardVirtualMethod(
            3,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                celt,
                rgvar == null ? (Parameter)PTR_NULL : new Pointer.Const(rgvar),
                pceltFetched == null ? (Parameter)PTR_NULL : new Pointer(pceltFetched)
            }
        );
    }

    /**
     * 
     */
    public void skip(
        UInt32 /*[in]*/ celt)
        throws ComException
    {
       invokeStandardVirtualMethod(
            4,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                celt
            }
        );
    }

    /**
     * 
     */
    public void reset()
        throws ComException
    {
       invokeStandardVirtualMethod(
            5,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    /**
     * 
     */
    public void clone_(
        IEnumVARIANT /*[out]*/ ppenum)
        throws ComException
    {
       invokeStandardVirtualMethod(
            6,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ppenum == null ? (Parameter)PTR_NULL : new Pointer((Parameter)ppenum)
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IEnumVARIANTImpl(this);
    }
}
