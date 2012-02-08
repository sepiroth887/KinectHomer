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
 * Represents Java interface for COM interface _ConstructorInfo.
 *
 *
 */
public interface _ConstructorInfo extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{E9A19478-9646-3679-9B10-8411AE1FD57D}";

    /**
     * 
     */
    void getTypeInfoCount(
        UInt32 /*[out]*/ pcTInfo)
        throws ComException;

    /**
     * 
     */
    void getTypeInfo(
        UInt32 /*[in]*/ iTInfo,
        UInt32 /*[in]*/ lcid,
        Int32 /*[in]*/ ppTInfo)
        throws ComException;

    /**
     * 
     */
    void getIDsOfNames(
        GUID /*[in]*/ riid,
        Int32 /*[in]*/ rgszNames,
        UInt32 /*[in]*/ cNames,
        UInt32 /*[in]*/ lcid,
        Int32 /*[in]*/ rgDispId)
        throws ComException;

    /**
     * 
     */
    void invoke(
        UInt32 /*[in]*/ dispIdMember,
        GUID /*[in]*/ riid,
        UInt32 /*[in]*/ lcid,
        Int16 /*[in]*/ wFlags,
        Int32 /*[in]*/ pDispParams,
        Int32 /*[in]*/ pVarResult,
        Int32 /*[in]*/ pExcepInfo,
        Int32 /*[in]*/ puArgErr)
        throws ComException;

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
    SafeArray getParameters()
        throws ComException;

    /**
     * 
     */
    MethodImplAttributes getMethodImplementationFlags()
        throws ComException;

    /**
     * 
     */
    Variant invoke_2(
        Variant /*[in]*/ obj,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ paramParameters,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    Variant invoke_3(
        Variant /*[in]*/ obj,
        SafeArray /*[in]*/ paramParameters)
        throws ComException;

    /**
     * 
     */
    Variant invoke_4(
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ paramParameters,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    Variant invoke_5(
        SafeArray /*[in]*/ paramParameters)
        throws ComException;

    /**
     * 
     */
    BStr getToString()
        throws ComException;

    /**
     * 
     */
    MemberTypes getMemberType()
        throws ComException;

    /**
     * 
     */
    BStr getName()
        throws ComException;

    /**
     * 
     */
    _Type getDeclaringType()
        throws ComException;

    /**
     * 
     */
    _Type getReflectedType()
        throws ComException;

    /**
     * 
     */
    RuntimeMethodHandle getMethodHandle()
        throws ComException;

    /**
     * 
     */
    MethodAttributes getAttributes()
        throws ComException;

    /**
     * 
     */
    CallingConventions getCallingConvention()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsPublic()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsPrivate()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsFamily()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsAssembly()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsFamilyAndAssembly()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsFamilyOrAssembly()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsStatic()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsFinal()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsVirtual()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsHideBySig()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsAbstract()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSpecialName()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsConstructor()
        throws ComException;
}
