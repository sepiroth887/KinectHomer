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
 * Represents Java interface for COM interface _Type.
 *
 *
 */
public interface _Type extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{BCA8B44D-AAD6-3A86-8AB7-03349F4F2DA2}";

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
    Int32 getArrayRank()
        throws ComException;

    /**
     * 
     */
    SafeArray getConstructors(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    _Type getInterface(
        BStr /*[in]*/ name,
        VariantBool /*[in]*/ ignoreCase)
        throws ComException;

    /**
     * 
     */
    SafeArray getInterfaces()
        throws ComException;

    /**
     * 
     */
    SafeArray findInterfaces(
        _TypeFilter /*[in]*/ filter,
        Variant /*[in]*/ filterCriteria)
        throws ComException;

    /**
     * 
     */
    _EventInfo getEvent(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getEvents()
        throws ComException;

    /**
     * 
     */
    SafeArray getEvents_2(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getNestedTypes(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    _Type getNestedType(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getMember(
        BStr /*[in]*/ name,
        MemberTypes /*[in]*/ Type,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getDefaultMembers()
        throws ComException;

    /**
     * 
     */
    SafeArray findMembers(
        MemberTypes /*[in]*/ MemberType,
        BindingFlags /*[in]*/ bindingAttr,
        _MemberFilter /*[in]*/ filter,
        Variant /*[in]*/ filterCriteria)
        throws ComException;

    /**
     * 
     */
    _Type getElementType()
        throws ComException;

    /**
     * 
     */
    VariantBool isSubclassOf(
        _Type /*[in]*/ c)
        throws ComException;

    /**
     * 
     */
    VariantBool isInstanceOfType(
        Variant /*[in]*/ o)
        throws ComException;

    /**
     * 
     */
    VariantBool isAssignableFrom(
        _Type /*[in]*/ c)
        throws ComException;

    /**
     * 
     */
    InterfaceMapping getInterfaceMap(
        _Type /*[in]*/ interfaceType)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getMethods(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    _FieldInfo getField(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getFields(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    SafeArray getProperties(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getMember_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    SafeArray getMembers(
        BindingFlags /*[in]*/ bindingAttr)
        throws ComException;

    /**
     * 
     */
    Variant invokeMember(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        Variant /*[in]*/ Target,
        SafeArray /*[in]*/ args,
        SafeArray /*[in]*/ modifiers,
        _CultureInfo /*[in]*/ culture,
        SafeArray /*[in]*/ namedParameters)
        throws ComException;

    /**
     * 
     */
    Variant invokeMember_2(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        Variant /*[in]*/ Target,
        SafeArray /*[in]*/ args,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    Variant invokeMember_3(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ invokeAttr,
        _Binder /*[in]*/ Binder,
        Variant /*[in]*/ Target,
        SafeArray /*[in]*/ args)
        throws ComException;

    /**
     * 
     */
    _ConstructorInfo getConstructor(
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        CallingConventions /*[in]*/ callConvention,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _ConstructorInfo getConstructor_2(
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _ConstructorInfo getConstructor_3(
        SafeArray /*[in]*/ types)
        throws ComException;

    /**
     * 
     */
    SafeArray getConstructors_2()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod_3(
        BStr /*[in]*/ name,
        BindingFlags /*[in]*/ bindingAttr,
        _Binder /*[in]*/ Binder,
        CallingConventions /*[in]*/ callConvention,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod_4(
        BStr /*[in]*/ name,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod_5(
        BStr /*[in]*/ name,
        SafeArray /*[in]*/ types)
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod_6(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getMethods_2()
        throws ComException;

    /**
     * 
     */
    _FieldInfo getField_2(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getFields_2()
        throws ComException;

    /**
     * 
     */
    _Type getInterface_2(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    _EventInfo getEvent_2(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty_3(
        BStr /*[in]*/ name,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty_4(
        BStr /*[in]*/ name,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ types)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty_5(
        BStr /*[in]*/ name,
        SafeArray /*[in]*/ types)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty_6(
        BStr /*[in]*/ name,
        _Type /*[in]*/ returnType)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo getProperty_7(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getProperties_2()
        throws ComException;

    /**
     * 
     */
    SafeArray getNestedTypes_2()
        throws ComException;

    /**
     * 
     */
    _Type getNestedType_2(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getMember_3(
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    SafeArray getMembers_2()
        throws ComException;

    /**
     * 
     */
    VariantBool equals_2(
        _Type /*[in]*/ o)
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
    GUID getGuid()
        throws ComException;

    /**
     * 
     */
    _Module getModule()
        throws ComException;

    /**
     * 
     */
    _Assembly getAssembly()
        throws ComException;

    /**
     * 
     */
    RuntimeTypeHandle getTypeHandle()
        throws ComException;

    /**
     * 
     */
    BStr getFullName()
        throws ComException;

    /**
     * 
     */
    BStr getNamespace()
        throws ComException;

    /**
     * 
     */
    BStr getAssemblyQualifiedName()
        throws ComException;

    /**
     * 
     */
    _Type getBaseType()
        throws ComException;

    /**
     * 
     */
    _Type getUnderlyingSystemType()
        throws ComException;

    /**
     * 
     */
    _ConstructorInfo getTypeInitializer()
        throws ComException;

    /**
     * 
     */
    TypeAttributes getAttributes()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNotPublic()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsPublic()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNestedPublic()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNestedPrivate()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNestedFamily()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNestedAssembly()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNestedFamANDAssem()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsNestedFamORAssem()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsAutoLayout()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsLayoutSequential()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsExplicitLayout()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsClass()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsInterface()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsValueType()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsAbstract()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSealed()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsEnum()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSpecialName()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsImport()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSerializable()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsAnsiClass()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsUnicodeClass()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsAutoClass()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsArray()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsByRef()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsPointer()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsPrimitive()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsCOMObject()
        throws ComException;

    /**
     * 
     */
    VariantBool getHasElementType()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsContextful()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsMarshalByRef()
        throws ComException;
}
