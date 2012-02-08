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
 * Represents Java interface for COM interface _EventInfo.
 *
 *
 */
public interface _EventInfo extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{9DE59C64-D889-35A1-B897-587D74469E5B}";

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
    _MethodInfo getAddMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getRemoveMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getRaiseMethod(
        VariantBool /*[in]*/ nonPublic)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getAddMethod_2()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getRemoveMethod_2()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getRaiseMethod_2()
        throws ComException;

    /**
     * 
     */
    void addEventHandler(
        Variant /*[in]*/ Target,
        _Delegate /*[in]*/ handler)
        throws ComException;

    /**
     * 
     */
    void removeEventHandler(
        Variant /*[in]*/ Target,
        _Delegate /*[in]*/ handler)
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
    EventAttributes getAttributes()
        throws ComException;

    /**
     * 
     */
    _Type getEventHandlerType()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSpecialName()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsMulticast()
        throws ComException;
}
