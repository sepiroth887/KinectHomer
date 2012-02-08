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
 * Represents COM interface _Assembly.
 *
 *
 */
public class _AssemblyImpl extends IDispatchImpl
    implements _Assembly
{
    public static final String INTERFACE_IDENTIFIER = _Assembly.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _AssemblyImpl()
    {
    }

    protected _AssemblyImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _AssemblyImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _AssemblyImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _AssemblyImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
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
    public _AssemblyName getName()
        throws ComException
    {
        _AssemblyNameImpl pRetVal = new _AssemblyNameImpl();
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
    public _AssemblyName getName_2(
        VariantBool /*[in]*/ copiedName)
        throws ComException
    {
        _AssemblyNameImpl pRetVal = new _AssemblyNameImpl();
       invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                copiedName,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Type getType_2(
        BStr /*[in]*/ name)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            17,
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
    public _Type getType_3(
        BStr /*[in]*/ name,
        VariantBool /*[in]*/ throwOnError)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            18,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                throwOnError,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getExportedTypes()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_TypeImpl.class);
       invokeStandardVirtualMethod(
            19,
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
    public SafeArray getTypes()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_TypeImpl.class);
       invokeStandardVirtualMethod(
            20,
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
    public _Stream getManifestResourceStream(
        _Type /*[in]*/ Type,
        BStr /*[in]*/ name)
        throws ComException
    {
        _StreamImpl pRetVal = new _StreamImpl();
       invokeStandardVirtualMethod(
            21,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Type == null ? (Parameter)PTR_NULL : (Parameter)Type,
                name == null ? (Parameter)PTR_NULL : name,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Stream getManifestResourceStream_2(
        BStr /*[in]*/ name)
        throws ComException
    {
        _StreamImpl pRetVal = new _StreamImpl();
       invokeStandardVirtualMethod(
            22,
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
    public _FileStream getFile(
        BStr /*[in]*/ name)
        throws ComException
    {
        _FileStreamImpl pRetVal = new _FileStreamImpl();
       invokeStandardVirtualMethod(
            23,
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
    public SafeArray getFiles()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_FileStreamImpl.class);
       invokeStandardVirtualMethod(
            24,
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
    public SafeArray getFiles_2(
        VariantBool /*[in]*/ getResourceModules)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_FileStreamImpl.class);
       invokeStandardVirtualMethod(
            25,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                getResourceModules,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getManifestResourceNames()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(BStr.class);
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
    public _ManifestResourceInfo getManifestResourceInfo(
        BStr /*[in]*/ resourceName)
        throws ComException
    {
        _ManifestResourceInfoImpl pRetVal = new _ManifestResourceInfoImpl();
       invokeStandardVirtualMethod(
            27,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                resourceName == null ? (Parameter)PTR_NULL : resourceName,
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
            30,
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
            31,
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
            32,
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
    public void getObjectData(
        _SerializationInfo /*[in]*/ info,
        StreamingContext /*[in]*/ Context)
        throws ComException
    {
       invokeStandardVirtualMethod(
            33,
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
    public void add_ModuleResolve(
        _ModuleResolveEventHandler /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            34,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                value == null ? (Parameter)PTR_NULL : (Parameter)value
            }
        );
    }

    /**
     * 
     */
    public void remove_ModuleResolve(
        _ModuleResolveEventHandler /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            35,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                value == null ? (Parameter)PTR_NULL : (Parameter)value
            }
        );
    }

    /**
     * 
     */
    public _Type getType_4(
        BStr /*[in]*/ name,
        VariantBool /*[in]*/ throwOnError,
        VariantBool /*[in]*/ ignoreCase)
        throws ComException
    {
        _TypeImpl pRetVal = new _TypeImpl();
       invokeStandardVirtualMethod(
            36,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                throwOnError,
                ignoreCase,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Assembly getSatelliteAssembly(
        _CultureInfo /*[in]*/ culture)
        throws ComException
    {
        _AssemblyImpl pRetVal = new _AssemblyImpl();
       invokeStandardVirtualMethod(
            37,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Assembly getSatelliteAssembly_2(
        _CultureInfo /*[in]*/ culture,
        _Version /*[in]*/ Version)
        throws ComException
    {
        _AssemblyImpl pRetVal = new _AssemblyImpl();
       invokeStandardVirtualMethod(
            38,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                Version == null ? (Parameter)PTR_NULL : (Parameter)Version,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Module loadModule(
        BStr /*[in]*/ moduleName,
        SafeArray /*[in]*/ rawModule)
        throws ComException
    {
        _ModuleImpl pRetVal = new _ModuleImpl();
       invokeStandardVirtualMethod(
            39,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                moduleName == null ? (Parameter)PTR_NULL : moduleName,
                rawModule,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Module loadModule_2(
        BStr /*[in]*/ moduleName,
        SafeArray /*[in]*/ rawModule,
        SafeArray /*[in]*/ rawSymbolStore)
        throws ComException
    {
        _ModuleImpl pRetVal = new _ModuleImpl();
       invokeStandardVirtualMethod(
            40,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                moduleName == null ? (Parameter)PTR_NULL : moduleName,
                rawModule,
                rawSymbolStore,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant createInstance(
        BStr /*[in]*/ typeName)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            41,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                typeName == null ? (Parameter)PTR_NULL : typeName,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant createInstance_2(
        BStr /*[in]*/ typeName,
        VariantBool /*[in]*/ ignoreCase)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            42,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                typeName == null ? (Parameter)PTR_NULL : typeName,
                ignoreCase,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public Variant createInstance_3(
        BStr /*[in]*/ typeName,
        VariantBool /*[in]*/ ignoreCase,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ args,
        _CultureInfo /*[in]*/ culture,
        SafeArray /*[in]*/ activationAttributes)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            43,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                typeName == null ? (Parameter)PTR_NULL : typeName,
                ignoreCase,
                bindingAttr,
                Binder == null ? (Parameter)PTR_NULL : (Parameter)Binder,
                args,
                culture == null ? (Parameter)PTR_NULL : (Parameter)culture,
                activationAttributes,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getLoadedModules()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ModuleImpl.class);
       invokeStandardVirtualMethod(
            44,
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
    public SafeArray getLoadedModules_2(
        VariantBool /*[in]*/ getResourceModules)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ModuleImpl.class);
       invokeStandardVirtualMethod(
            45,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                getResourceModules,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public SafeArray getModules()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ModuleImpl.class);
       invokeStandardVirtualMethod(
            46,
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
    public SafeArray getModules_2(
        VariantBool /*[in]*/ getResourceModules)
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_ModuleImpl.class);
       invokeStandardVirtualMethod(
            47,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                getResourceModules,
                new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public _Module getModule(
        BStr /*[in]*/ name)
        throws ComException
    {
        _ModuleImpl pRetVal = new _ModuleImpl();
       invokeStandardVirtualMethod(
            48,
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
    public SafeArray getReferencedAssemblies()
        throws ComException
    {
        SafeArray pRetVal = new SafeArray(_AssemblyNameImpl.class);
       invokeStandardVirtualMethod(
            49,
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
    public BStr getCodeBase()
        throws ComException
    {
        BStr pRetVal = new BStr();
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
    public BStr getEscapedCodeBase()
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
    public BStr getFullName()
        throws ComException
    {
        BStr pRetVal = new BStr();
       invokeStandardVirtualMethod(
            15,
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
    public _MethodInfo getEntryPoint()
        throws ComException
    {
        _MethodInfoImpl pRetVal = new _MethodInfoImpl();
       invokeStandardVirtualMethod(
            16,
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
    public BStr getLocation()
        throws ComException
    {
        BStr pRetVal = new BStr();
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
    public _Evidence getEvidence()
        throws ComException
    {
        _EvidenceImpl pRetVal = new _EvidenceImpl();
       invokeStandardVirtualMethod(
            29,
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
    public VariantBool getGlobalAssemblyCache()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            50,
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
        return new _AssemblyImpl(this);
    }
}
