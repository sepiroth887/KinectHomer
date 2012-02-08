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
 * Represents Java interface for COM interface ICloneable.
 *
 *
 */
public interface ICloneable extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{0CB251A7-3AB3-3B5C-A0B8-9DDF88824B85}";

    /**
     * 
     */
    Variant clone_()
        throws ComException;
}
