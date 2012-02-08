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
 * Represents Java interface for COM interface _Delegate.
 *
 *
 */
public interface _Delegate extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{FB6AB00F-5096-3AF8-A33D-D7885A5FA829}";

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
    SafeArray getInvocationList()
        throws ComException;

    /**
     * 
     */
    Variant clone_()
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
    Variant dynamicInvoke(
        SafeArray /*[in]*/ args)
        throws ComException;

    /**
     * 
     */
    BStr getToString()
        throws ComException;

    /**
     * 
     */
    _MethodInfo getMethod()
        throws ComException;

    /**
     * 
     */
    Variant getTarget()
        throws ComException;
}
