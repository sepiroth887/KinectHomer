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
 * Represents Java interface for COM interface IDictionary.
 *
 *
 */
public interface IDictionary extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{6A6841DF-3287-3D87-8060-CE0B4C77D2A1}";

    /**
     * 
     */
    VariantBool contains(
        Variant /*[in]*/ key)
        throws ComException;

    /**
     * 
     */
    void add(
        Variant /*[in]*/ key,
        Variant /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    void clear()
        throws ComException;

    /**
     * 
     */
    IDictionaryEnumerator getEnumerator()
        throws ComException;

    /**
     * 
     */
    void remove(
        Variant /*[in]*/ key)
        throws ComException;

    /**
     * 
     */
    Variant getItem(
        Variant /*[in]*/ key)
        throws ComException;

    /**
     * 
     */
    void setItem(
        Variant /*[in]*/ key,
        Variant /*[in]*/ pRetVal)
        throws ComException;

    /**
     * 
     */
    ICollection getKeys()
        throws ComException;

    /**
     * 
     */
    ICollection getValues()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsReadOnly()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsFixedSize()
        throws ComException;
}
