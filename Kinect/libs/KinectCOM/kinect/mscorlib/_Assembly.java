package kinect.mscorlib;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.impl.*;

/**
 * Represents Java interface for COM interface _Assembly.
 *
 *
 */
public interface _Assembly extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{17156360-2F1A-384A-BC52-FDE93C215C5B}";

    /**
     * 
     */
    VariantBool equals_(
        Variant /*[in]*/ other)
        throws ComException;

    /**
     * 
     */
    Int32 getHashCode()
        throws ComException;

    /**
     * 
     */
    _Type getType()
        throws ComException;

    /**
     * 
     */
    _AssemblyName getName()
        throws ComException;

    /**
     * 
     */
    _AssemblyName getName_2(
        VariantBool /*[in]*/ copiedName)
        throws ComException;

    /**
     * 
     */
    _Type getType_2(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    _Type getType_3(
        BStr /*[in]*/ name,
        VariantBool /*[in]*/ throwOnError)
        throws ComException;

    /**
     * 
     */
    SafeArray getExportedTypes()
        throws ComException;

    /**
     * 
     */
    SafeArray getTypes()
        throws ComException;

    /**
     * 
     */
    _Stream getManifestResourceStream(
        _Type /*[in]*/ Type,
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    _Stream getManifestResourceStream_2(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    _FileStream getFile(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getFiles()
        throws ComException;

    /**
     * 
     */
    SafeArray getFiles_2(
        VariantBool /*[in]*/ getResourceModules)
        throws ComException;

    /**
     * 
     */
    SafeArray getManifestResourceNames()
        throws ComException;

    /**
     * 
     */
    _ManifestResourceInfo getManifestResourceInfo(
        BStr /*[in]*/ resourceName)
        throws ComException;

    /**
     * 
     */
    SafeArray getCustomAttributes(
        _Type /*[in]*/ attributeType,
        VariantBool /*[in]*/ inherit)
        throws ComException;

    /**
     * 
     */
    SafeArray getCustomAttributes_2(
        VariantBool /*[in]*/ inherit)
        throws ComException;

    /**
     * 
     */
    VariantBool isDefined(
        _Type /*[in]*/ attributeType,
        VariantBool /*[in]*/ inherit)
        throws ComException;

    /**
     * 
     */
    void getObjectData(
        _SerializationInfo /*[in]*/ info,
        StreamingContext /*[in]*/ Context)
        throws ComException;

    /**
     * 
     */
    void add_ModuleResolve(
        _ModuleResolveEventHandler /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    void remove_ModuleResolve(
        _ModuleResolveEventHandler /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    _Type getType_4(
        BStr /*[in]*/ name,
        VariantBool /*[in]*/ throwOnError,
        VariantBool /*[in]*/ ignoreCase)
        throws ComException;

    /**
     * 
     */
    _Assembly getSatelliteAssembly(
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    _Assembly getSatelliteAssembly_2(
        _CultureInfo /*[in]*/ culture,
        _Version /*[in]*/ Version)
        throws ComException;

    /**
     * 
     */
    _Module loadModule(
        BStr /*[in]*/ moduleName,
        SafeArray /*[in]*/ rawModule)
        throws ComException;

    /**
     * 
     */
    _Module loadModule_2(
        BStr /*[in]*/ moduleName,
        SafeArray /*[in]*/ rawModule,
        SafeArray /*[in]*/ rawSymbolStore)
        throws ComException;

    /**
     * 
     */
    Variant createInstance(
        BStr /*[in]*/ typeName)
        throws ComException;

    /**
     * 
     */
    Variant createInstance_2(
        BStr /*[in]*/ typeName,
        VariantBool /*[in]*/ ignoreCase)
        throws ComException;

    /**
     * 
     */
    Variant createInstance_3(
        BStr /*[in]*/ typeName,
        VariantBool /*[in]*/ ignoreCase,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ args,
        _CultureInfo /*[in]*/ culture,
        SafeArray /*[in]*/ activationAttributes)
        throws ComException;

    /**
     * 
     */
    SafeArray getLoadedModules()
        throws ComException;

    /**
     * 
     */
    SafeArray getLoadedModules_2(
        VariantBool /*[in]*/ getResourceModules)
        throws ComException;

    /**
     * 
     */
    SafeArray getModules()
        throws ComException;

    /**
     * 
     */
    SafeArray getModules_2(
        VariantBool /*[in]*/ getResourceModules)
        throws ComException;

    /**
     * 
     */
    _Module getModule(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getReferencedAssemblies()
        throws ComException;

    /**
     * 
     */
    BStr getToString()
        throws ComException;

    /**
     * 
     */
    BStr getCodeBase()
        throws ComException;

    /**
     * 
     */
    BStr getEscapedCodeBase()
        throws ComException;

    /**
     * 
     */
    BStr getFullName()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getEntryPoint()
        throws ComException;

    /**
     * 
     */
    BStr getLocation()
        throws ComException;

    /**
     * 
     */
    _Evidence getEvidence()
        throws ComException;

    /**
     * 
     */
    VariantBool getGlobalAssemblyCache()
        throws ComException;
}
