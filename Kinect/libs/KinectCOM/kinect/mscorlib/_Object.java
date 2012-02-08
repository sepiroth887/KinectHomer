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
 * Represents Java interface for COM interface _Object.
 *
 *
 */
public interface _Object extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{65074F7F-63C0-304E-AF0A-D51741CB4A8D}";

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
    BStr getToString()
        throws ComException;
}
