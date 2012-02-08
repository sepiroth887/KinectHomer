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
 * Represents COM interface _PropertyInfo.
 *
 *
 */
public class _PropertyInfoImpl extends IUnknownImpl
    implements _PropertyInfo
{
    public static final String INTERFACE_IDENTIFIER = _PropertyInfo.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _PropertyInfoImpl()
    {
    }

    protected _PropertyInfoImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _PropertyInfoImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _PropertyInfoImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _PropertyInfoImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
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

    /**
     * 
     */
    public VariantBool equals_(
        Variant /*[in]*/ other)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                other,
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
    public SafeArray getCustomAttributes(
        _Type /*[in]*/ attributeType,
        VariantBool /*[in]*/ inherit)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(Variant.class);
       invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                attributeType == null ? (Parameter)PTR_NULL : (Parameter)attributeType,
                inherit,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getCustomAttributes_2(
        VariantBool /*[in]*/ inherit)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(Variant.class);
       invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                inherit,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public VariantBool isDefined(
        _Type /*[in]*/ attributeType,
        VariantBool /*[in]*/ inherit)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            17,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                attributeType == null ? (Parameter)PTR_NULL : (Parameter)attributeType,
                inherit,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant getValue(
        Variant /*[in]*/ obj,
        SafeArray /*[in]*/ index)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            19,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                index,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant getValue_2(
        Variant /*[in]*/ obj,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ index,
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            20,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                invokeAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                index,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void setValue(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value,
        SafeArray /*[in]*/ index)
        throws ComException
    {
       invokeStandardVirtualMethod(
            21,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                value,
                index
            }
        );
    }

    /**
     * 
     */
    public void setValue_2(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ index,
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
       invokeStandardVirtualMethod(
            22,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                value,
                invokeAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                index,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture
            }
        );
    }

    /**
     * 
     */
    public SafeArray getAccessors(
        VariantBool /*[in]*/ nonPublic)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MethodInfoImpl.class);
       invokeStandardVirtualMethod(
            23,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                nonPublic,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _MethodInfo getGetMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            24,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                nonPublic,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _MethodInfo getSetMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            25,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                nonPublic,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getIndexParameters()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ParameterInfoImpl.class);
       invokeStandardVirtualMethod(
            26,
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
    public SafeArray getAccessors_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MethodInfoImpl.class);
       invokeStandardVirtualMethod(
            30,
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
    public _MethodInfo getGetMethod_2()
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            31,
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
    public _MethodInfo getSetMethod_2()
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            32,
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
    public MemberTypes getMemberType()
        throws ComException
    {
        MemberTypes pRetVal = new MemberTypes();
       invokeStandardVirtualMethod(
            11,
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
    public BStr getName()
        throws ComException
    {
        BStr pRetVal = new BStr();
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
    public _Type getDeclaringType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            13,
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
    public _Type getReflectedType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            14,
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
    public _Type getPropertyType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            18,
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
    public PropertyAttributes getAttributes()
        throws ComException
    {
        PropertyAttributes pRetVal = new PropertyAttributes();
       invokeStandardVirtualMethod(
            27,
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
    public VariantBool getCanRead()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            28,
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
    public VariantBool getCanWrite()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            29,
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
    public VariantBool getIsSpecialName()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            33,
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
        return new _PropertyInfoImpl(this);
    }
}
