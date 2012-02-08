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
 * Represents Java interface for COM interface _PropertyInfo.
 *
 *
 */
public interface _PropertyInfo extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{F59ED4E4-E68F-3218-BD77-061AA82824BF}";

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
    Variant getValue(
        Variant /*[in]*/ obj,
        SafeArray /*[in]*/ index)
        throws ComException;

    /**
     * 
     */
    Variant getValue_2(
        Variant /*[in]*/ obj,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ index,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    void setValue(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value,
        SafeArray /*[in]*/ index)
        throws ComException;

    /**
     * 
     */
    void setValue_2(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ index,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    SafeArray getAccessors(
        VariantBool /*[in]*/ nonPublic)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getGetMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getSetMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException;

    /**
     * 
     */
    SafeArray getIndexParameters()
        throws ComException;

    /**
     * 
     */
    SafeArray getAccessors_2()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getGetMethod_2()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getSetMethod_2()
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
    _Type getPropertyType()
        throws ComException;

    /**
     * 
     */
    PropertyAttributes getAttributes()
        throws ComException;

    /**
     * 
     */
    VariantBool getCanRead()
        throws ComException;

    /**
     * 
     */
    VariantBool getCanWrite()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSpecialName()
        throws ComException;
}
