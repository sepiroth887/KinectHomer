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
 * Represents Java interface for COM interface _FieldInfo.
 *
 *
 */
public interface _FieldInfo extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{8A7C1442-A9FB-366B-80D8-4939FFA6DBE0}";

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
        Variant /*[in]*/ obj)
        throws ComException;

    /**
     * 
     */
    Variant getValueDirect(
        Variant /*[in]*/ obj)
        throws ComException;

    /**
     * 
     */
    void setValue(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    void setValueDirect(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    void setValue_2(
        Variant /*[in]*/ obj,
        Variant /*[in]*/ value)
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
    _Type getFieldType()
        throws ComException;

    /**
     * 
     */
    RuntimeFieldHandle getFieldHandle()
        throws ComException;

    /**
     * 
     */
    FieldAttributes getAttributes()
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
    VariantBool getIsInitOnly()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsLiteral()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNotSerialized()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSpecialName()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsPinvokeImpl()
        throws ComException;
}
