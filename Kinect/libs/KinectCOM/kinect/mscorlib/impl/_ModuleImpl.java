package kinect.mscorlib.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.*;

/**
 * Represents COM interface _Module.
 *
 *
 */
public class _ModuleImpl extends IUnknownImpl
    implements _Module
{
    public static final String INTERFACE_IDENTIFIER = _Module.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _ModuleImpl()
    {
    }

    protected _ModuleImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _ModuleImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _ModuleImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _ModuleImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void getTypeInfoCount(
        UInt32 /*[out]*/ pcTInfo)
        throws ComException
    {
       invokeStandardVirtualMethod(
            3,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pcTInfo == null ? (Parameter)PTR_NULL : new Pointer(pcTInfo)
            }
        );
    }

    /**
     * 
     */
    public void getTypeInfo(
        UInt32 /*[in]*/ iTInfo,
        UInt32 /*[in]*/ lcid,
        Int32 /*[in]*/ ppTInfo)
        throws ComException
    {
       invokeStandardVirtualMethod(
            4,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                iTInfo,
                lcid,
                ppTInfo
            }
        );
    }

    /**
     * 
     */
    public void getIDsOfNames(
        GUID /*[in]*/ riid,
        Int32 /*[in]*/ rgszNames,
        UInt32 /*[in]*/ cNames,
        UInt32 /*[in]*/ lcid,
        Int32 /*[in]*/ rgDispId)
        throws ComException
    {
       invokeStandardVirtualMethod(
            5,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                riid == null ? (Parameter)PTR_NULL : new Pointer.Const(riid),
                rgszNames,
                cNames,
                lcid,
                rgDispId
            }
        );
    }

    /**
     * 
     */
    public void invoke(
        UInt32 /*[in]*/ dispIdMember,
        GUID /*[in]*/ riid,
        UInt32 /*[in]*/ lcid,
        Int16 /*[in]*/ wFlags,
        Int32 /*[in]*/ pDispParams,
        Int32 /*[in]*/ pVarResult,
        Int32 /*[in]*/ pExcepInfo,
        Int32 /*[in]*/ puArgErr)
        throws ComException
    {
       invokeStandardVirtualMethod(
            6,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                dispIdMember,
                riid == null ? (Parameter)PTR_NULL : new Pointer.Const(riid),
                lcid,
                wFlags,
                pDispParams,
                pVarResult,
                pExcepInfo,
                puArgErr
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _ModuleImpl(this);
    }
}
