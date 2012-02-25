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
import kinect.stdole.*;
import kinect.stdole.impl.*;

/**
 * Represents Java interface for COM interface IEnumerable.
 *
 *
 */
public interface IEnumerable extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{496B0ABE-CDEE-11D3-88E8-00902754C43A}";

    /**
     * 
     */
    IEnumVARIANT getEnumerator()
        throws ComException;
}
