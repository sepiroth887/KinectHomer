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
 * Represents Java interface for COM interface ICustomAttributeProvider.
 *
 *
 */
public interface ICustomAttributeProvider extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{B9B91146-D6C2-3A62-8159-C2D1794CDEB0}";

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
}
