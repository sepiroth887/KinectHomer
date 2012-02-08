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
 * Represents COM interface _Binder.
 *
 *
 */
public class _BinderImpl extends IDispatchImpl
    implements _Binder
{
    public static final String INTERFACE_IDENTIFIER = _Binder.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _BinderImpl()
    {
    }

    protected _BinderImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _BinderImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _BinderImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _BinderImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
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
    public _MethodBase bindToMethod(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        SafeArray /*[in,out]*/ args,
        SafeArray /*[in]*/ modifiers,
        _CultureInfo /*[in]*/ culture,
        SafeArray /*[in]*/ names,
        Variant /*[out]*/ state)
        throws ComException
    {
        _MethodBaseImpl pRetVal = new _MethodBaseImpl();
       invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                match,
                new Pointer(args),
                modifiers,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                names,
                state == null ? (Parameter)PTR_NULL : new Pointer(state),
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _FieldInfo bindToField(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        Variant /*[in]*/ value,
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
        _FieldInfoImpl pRetVal = new _FieldInfoImpl();
       invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                match,
                value,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _MethodBase selectMethod(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _MethodBaseImpl pRetVal = new _MethodBaseImpl();
       invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                match,
                types,
                modifiers,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo selectProperty(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ indexes,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                match,
                returnType == null ? (Parameter)PTR_NULL : (Parameter)returnType,
                indexes,
                modifiers,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant changeType(
        Variant /*[in]*/ value,
        _Type /*[in]*/ Type,
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                value,
                Type == null ? (Parameter)PTR_NULL : (Parameter)Type,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void reorderArgumentArray(
        SafeArray /*[in,out]*/ args,
        Variant /*[in]*/ state)
        throws ComException
    {
       invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                new Pointer(args),
                state
            }
        );
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

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _BinderImpl(this);
    }
}
