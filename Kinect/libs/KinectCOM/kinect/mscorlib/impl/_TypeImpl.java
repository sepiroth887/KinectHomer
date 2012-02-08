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
 * Represents COM interface _Type.
 *
 *
 */
public class _TypeImpl extends IUnknownImpl
    implements _Type
{
    public static final String INTERFACE_IDENTIFIER = _Type.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _TypeImpl()
    {
    }

    protected _TypeImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _TypeImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _TypeImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _TypeImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
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
    public Int32 getArrayRank()
        throws ComException
    {
        Int32 pRetVal = new Int32();
       invokeStandardVirtualMethod(
            25,
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
    public SafeArray getConstructors(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ConstructorInfoImpl.class);
       invokeStandardVirtualMethod(
            27,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Type getInterface(
        BStr /*[in]*/ name,
        VariantBool /*[in]*/ ignoreCase)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            28,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                ignoreCase,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getInterfaces()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_TypeImpl.class);
       invokeStandardVirtualMethod(
            29,
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
    public SafeArray findInterfaces(
        _TypeFilter /*[in]*/ filter,
        Variant /*[in]*/ filterCriteria)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_TypeImpl.class);
       invokeStandardVirtualMethod(
            30,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                filter == null ? (Parameter)PTR_NULL : (Parameter)filter,
                filterCriteria,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _EventInfo getEvent(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        _EventInfoImpl pRetVal = new _EventInfoImpl();
       invokeStandardVirtualMethod(
            31,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getEvents()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_EventInfoImpl.class);
       invokeStandardVirtualMethod(
            32,
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
    public SafeArray getEvents_2(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_EventInfoImpl.class);
       invokeStandardVirtualMethod(
            33,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getNestedTypes(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_TypeImpl.class);
       invokeStandardVirtualMethod(
            34,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Type getNestedType(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            35,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMember(
        BStr /*[in]*/ name,
        MemberTypes /*[in]*/ Type,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            36,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                Type,
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getDefaultMembers()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            37,
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
    public SafeArray findMembers(
        MemberTypes /*[in]*/ MemberType,
        BindingFlags /*[in]*/ bindingAttr,
        _MemberFilter /*[in]*/ filter,
        Variant /*[in]*/ filterCriteria)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            38,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                MemberType,
                bindingAttr,
                filter == null ? (Parameter)PTR_NULL : (Parameter)filter,
                filterCriteria,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Type getElementType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            39,
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
    public VariantBool isSubclassOf(
        _Type /*[in]*/ c)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            40,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                c == null ? (Parameter)PTR_NULL : (Parameter)c,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public VariantBool isInstanceOfType(
        Variant /*[in]*/ o)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            41,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                o,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public VariantBool isAssignableFrom(
        _Type /*[in]*/ c)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            42,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                c == null ? (Parameter)PTR_NULL : (Parameter)c,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public InterfaceMapping getInterfaceMap(
        _Type /*[in]*/ interfaceType)
        throws ComException
    {
        InterfaceMapping pRetVal = new InterfaceMapping();
       invokeStandardVirtualMethod(
            43,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                interfaceType == null ? (Parameter)PTR_NULL : (Parameter)interfaceType,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _MethodInfo getMethod(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            44,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
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
    public _MethodInfo getMethod_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            45,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMethods(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MethodInfoImpl.class);
       invokeStandardVirtualMethod(
            46,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _FieldInfo getField(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        _FieldInfoImpl pRetVal = new _FieldInfoImpl();
       invokeStandardVirtualMethod(
            47,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getFields(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_FieldInfoImpl.class);
       invokeStandardVirtualMethod(
            48,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo getProperty(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            49,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo getProperty_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            50,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                returnType == null ? (Parameter)PTR_NULL : (Parameter)returnType,
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
    public SafeArray getProperties(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_PropertyInfoImpl.class);
       invokeStandardVirtualMethod(
            51,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMember_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            52,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMembers(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            53,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant invokeMember(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        Variant /*[in]*/ Target,
        SafeArray /*[in]*/ args,
        SafeArray /*[in]*/ modifiers,
        _CultureInfo /*[in]*/ culture,
        SafeArray /*[in]*/ namedParameters)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            54,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                invokeAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                Target,
                args,
                modifiers,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                namedParameters,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant invokeMember_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        Variant /*[in]*/ Target,
        SafeArray /*[in]*/ args,
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            56,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                invokeAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                Target,
                args,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant invokeMember_3(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        Variant /*[in]*/ Target,
        SafeArray /*[in]*/ args)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            57,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                invokeAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                Target,
                args,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _ConstructorInfo getConstructor(
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        CallingConventions /*[in]*/ callConvention,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _ConstructorInfoImpl pRetVal = new _ConstructorInfoImpl();
       invokeStandardVirtualMethod(
            58,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                callConvention,
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
    public _ConstructorInfo getConstructor_2(
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _ConstructorInfoImpl pRetVal = new _ConstructorInfoImpl();
       invokeStandardVirtualMethod(
            59,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                bindingAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
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
    public _ConstructorInfo getConstructor_3(
        SafeArray /*[in]*/ types)
        throws ComException
    {
        _ConstructorInfoImpl pRetVal = new _ConstructorInfoImpl();
       invokeStandardVirtualMethod(
            60,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                types,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getConstructors_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ConstructorInfoImpl.class);
       invokeStandardVirtualMethod(
            61,
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
    public _MethodInfo getMethod_3(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        CallingConventions /*[in]*/ callConvention,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            63,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                bindingAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                callConvention,
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
    public _MethodInfo getMethod_4(
        BStr /*[in]*/ name,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            64,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
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
    public _MethodInfo getMethod_5(
        BStr /*[in]*/ name,
        SafeArray /*[in]*/ types)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            65,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                types,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _MethodInfo getMethod_6(
        BStr /*[in]*/ name)
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            66,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMethods_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MethodInfoImpl.class);
       invokeStandardVirtualMethod(
            67,
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
    public _FieldInfo getField_2(
        BStr /*[in]*/ name)
        throws ComException
    {
        _FieldInfoImpl pRetVal = new _FieldInfoImpl();
       invokeStandardVirtualMethod(
            68,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getFields_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_FieldInfoImpl.class);
       invokeStandardVirtualMethod(
            69,
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
    public _Type getInterface_2(
        BStr /*[in]*/ name)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            70,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _EventInfo getEvent_2(
        BStr /*[in]*/ name)
        throws ComException
    {
        _EventInfoImpl pRetVal = new _EventInfoImpl();
       invokeStandardVirtualMethod(
            71,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo getProperty_3(
        BStr /*[in]*/ name,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            72,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                returnType == null ? (Parameter)PTR_NULL : (Parameter)returnType,
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
    public _PropertyInfo getProperty_4(
        BStr /*[in]*/ name,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ types)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            73,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                returnType == null ? (Parameter)PTR_NULL : (Parameter)returnType,
                types,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo getProperty_5(
        BStr /*[in]*/ name,
        SafeArray /*[in]*/ types)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            74,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                types,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo getProperty_6(
        BStr /*[in]*/ name,
        _Type /*[in]*/ returnType)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            75,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                returnType == null ? (Parameter)PTR_NULL : (Parameter)returnType,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _PropertyInfo getProperty_7(
        BStr /*[in]*/ name)
        throws ComException
    {
        _PropertyInfoImpl pRetVal = new _PropertyInfoImpl();
       invokeStandardVirtualMethod(
            76,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getProperties_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_PropertyInfoImpl.class);
       invokeStandardVirtualMethod(
            77,
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
    public SafeArray getNestedTypes_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_TypeImpl.class);
       invokeStandardVirtualMethod(
            78,
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
    public _Type getNestedType_2(
        BStr /*[in]*/ name)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            79,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMember_3(
        BStr /*[in]*/ name)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            80,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getMembers_2()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_MemberInfoImpl.class);
       invokeStandardVirtualMethod(
            81,
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
    public VariantBool equals_2(
        _Type /*[in]*/ o)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            114,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                o == null ? (Parameter)PTR_NULL : (Parameter)o,
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
    public GUID getGuid()
        throws ComException
    {
        GUID pRetVal = new GUID();
       invokeStandardVirtualMethod(
            18,
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
    public _Module getModule()
        throws ComException
    {
        _ModuleImpl pRetVal = new _ModuleImpl();
       invokeStandardVirtualMethod(
            19,
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
    public _Assembly getAssembly()
        throws ComException
    {
        _AssemblyImpl pRetVal = new _AssemblyImpl();
       invokeStandardVirtualMethod(
            20,
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
    public RuntimeTypeHandle getTypeHandle()
        throws ComException
    {
        RuntimeTypeHandle pRetVal = new RuntimeTypeHandle();
       invokeStandardVirtualMethod(
            21,
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
    public BStr getFullName()
        throws ComException
    {
        BStr pRetVal = new BStr();
       invokeStandardVirtualMethod(
            22,
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
    public BStr getNamespace()
        throws ComException
    {
        BStr pRetVal = new BStr();
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
    public BStr getAssemblyQualifiedName()
        throws ComException
    {
        BStr pRetVal = new BStr();
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
    public _Type getBaseType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            26,
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
    public _Type getUnderlyingSystemType()
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            55,
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
    public _ConstructorInfo getTypeInitializer()
        throws ComException
    {
        _ConstructorInfoImpl pRetVal = new _ConstructorInfoImpl();
       invokeStandardVirtualMethod(
            62,
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
    public TypeAttributes getAttributes()
        throws ComException
    {
        TypeAttributes pRetVal = new TypeAttributes();
       invokeStandardVirtualMethod(
            82,
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
    public VariantBool getIsNotPublic()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            83,
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
            84,
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
    public VariantBool getIsNestedPublic()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            85,
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
    public VariantBool getIsNestedPrivate()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            86,
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
    public VariantBool getIsNestedFamily()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            87,
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
    public VariantBool getIsNestedAssembly()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            88,
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
    public VariantBool getIsNestedFamANDAssem()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            89,
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
    public VariantBool getIsNestedFamORAssem()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            90,
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
    public VariantBool getIsAutoLayout()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            91,
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
    public VariantBool getIsLayoutSequential()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            92,
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
    public VariantBool getIsExplicitLayout()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            93,
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
    public VariantBool getIsClass()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            94,
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
    public VariantBool getIsInterface()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            95,
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
    public VariantBool getIsValueType()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            96,
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
    public VariantBool getIsAbstract()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            97,
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
    public VariantBool getIsSealed()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            98,
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
    public VariantBool getIsEnum()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            99,
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
            100,
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
    public VariantBool getIsImport()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            101,
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
    public VariantBool getIsSerializable()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            102,
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
    public VariantBool getIsAnsiClass()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            103,
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
    public VariantBool getIsUnicodeClass()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            104,
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
    public VariantBool getIsAutoClass()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            105,
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
    public VariantBool getIsArray()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            106,
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
    public VariantBool getIsByRef()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            107,
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
    public VariantBool getIsPointer()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            108,
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
    public VariantBool getIsPrimitive()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            109,
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
    public VariantBool getIsCOMObject()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            110,
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
    public VariantBool getHasElementType()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            111,
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
    public VariantBool getIsContextful()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            112,
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
    public VariantBool getIsMarshalByRef()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            113,
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
        return new _TypeImpl(this);
    }
}
