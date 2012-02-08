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
 * Represents COM interface _FieldInfo.
 *
 *
 */
public class _FieldInfoImpl extends IUnknownImpl
    implements _FieldInfo
{
    public static final String INTERFACE_IDENTIFIER = _FieldInfo.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _FieldInfoImpl()
    {
    }

    protected _FieldInfoImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _FieldInfoImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _FieldInfoImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _FieldInfoImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
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
        Variant /*[in]*/ obj)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            19,
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
    public Variant getValueDirect(
        Variant /*[in]*/ obj)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            20,
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
    public void setValue(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
       invokeStandardVirtualMethod(
            21,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                value,
                invokeAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture
            }
        );
    }

    /**
     * 
     */
    public void setValueDirect(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            22,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                value
            }
        );
    }

    /**
     * 
     */
    public void setValue_2(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            25,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                obj,
                value
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
    public _Type getFieldType()
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
    public RuntimeFieldHandle getFieldHandle()
        throws ComException
    {
        RuntimeFieldHandle pRetVal = new RuntimeFieldHandle();
       invokeStandardVirtualMethod(
            23,
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
    public FieldAttributes getAttributes()
        throws ComException
    {
        FieldAttributes pRetVal = new FieldAttributes();
       invokeStandardVirtualMethod(
            24,
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
    public VariantBool getIsPublic()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            26,
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
    public VariantBool getIsPrivate()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
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
    public VariantBool getIsFamily()
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
    public VariantBool getIsAssembly()
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
    public VariantBool getIsFamilyAndAssembly()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            30,
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
    public VariantBool getIsFamilyOrAssembly()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            31,
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
    public VariantBool getIsStatic()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            32,
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
    public VariantBool getIsInitOnly()
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

    /**
     * 
     */
    public VariantBool getIsLiteral()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            34,
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
    public VariantBool getIsNotSerialized()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            35,
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
            36,
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
    public VariantBool getIsPinvokeImpl()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            37,
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
        return new _FieldInfoImpl(this);
    }
}
