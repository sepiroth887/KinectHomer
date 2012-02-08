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
 * Represents Java interface for COM interface _Binder.
 *
 *
 */
public interface _Binder extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{3169AB11-7109-3808-9A61-EF4BA0534FD9}";

    /**
     * 
     */
    VariantBool equals_(
        Variant /*[in]*/ obj)
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
    _MethodBase bindToMethod(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        SafeArray /*[in,out]*/ args,
        SafeArray /*[in]*/ modifiers,
        _CultureInfo /*[in]*/ culture,
        SafeArray /*[in]*/ names,
        Variant /*[out]*/ state)
        throws ComException;

    /**
     * 
     */
    _FieldInfo bindToField(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        Variant /*[in]*/ value,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    _MethodBase selectMethod(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        SafeArray /*[in]*/ types,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    _PropertyInfo selectProperty(
        BindingFlags /*[in]*/ bindingAttr,
        SafeArray /*[in]*/ match,
        _Type /*[in]*/ returnType,
        SafeArray /*[in]*/ indexes,
        SafeArray /*[in]*/ modifiers)
        throws ComException;

    /**
     * 
     */
    Variant changeType(
        Variant /*[in]*/ value,
        _Type /*[in]*/ Type,
        _CultureInfo /*[in]*/ culture)
        throws ComException;

    /**
     * 
     */
    void reorderArgumentArray(
        SafeArray /*[in,out]*/ args,
        Variant /*[in]*/ state)
        throws ComException;

    /**
     * 
     */
    BStr getToString()
        throws ComException;
}
