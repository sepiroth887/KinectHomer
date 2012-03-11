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
 * Represents Java interface for COM interface IDisposable.
 *
 *
 */
public interface IDisposable extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{805D7A98-D4AF-3F0F-967F-E5CF45312D2C}";

    /**
     * 
     */
    void dispose()
        throws ComException;
}
