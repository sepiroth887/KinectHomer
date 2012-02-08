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
 * Represents COM interface _Delegate.
 *
 *
 */
public class _DelegateImpl extends IDispatchImpl
    implements _Delegate
{
    public static final String INTERFACE_IDENTIFIER = _Delegate.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _DelegateImpl()
    {
    }

    protected _DelegateImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _DelegateImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _DelegateImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _DelegateImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public VariantBool equals_(
        Variant /*[in]*/ obj)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Int32 getHashCode()
        throws ComException
    {
        Int32 pRetVal = new Int32();
       invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Type getType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getInvocationList()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_DelegateImpl.class);
       invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant clone_()
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void getObjectData(
        _SerializationInfo /*[in]*/ info,
        StreamingContext /*[in]*/ Context)
        throws ComException
    {
       invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                info == null ? (Parameter)PTR_NULL : (Parameter)info,
                Context
            }
        );
    }

    /**
     * 
     */
    public Variant dynamicInvoke(
        SafeArray /*[in]*/ args)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                args,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public BStr getToString()
        throws ComException
    {
        BStr pRetVal = new BStr();
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _MethodInfo getMethod()
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant getTarget()
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
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
        return new _DelegateImpl(this);
    }
}
