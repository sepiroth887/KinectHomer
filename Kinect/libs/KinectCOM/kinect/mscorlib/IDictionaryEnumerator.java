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
 * Represents Java interface for COM interface IDictionaryEnumerator.
 *
 *
 */
public interface IDictionaryEnumerator extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{35D574BF-7A4F-3588-8C19-12212A0FE4DC}";

    /**
     * 
     */
    Variant getKey()
        throws ComException;

    /**
     * 
     */
    Variant getValue()
        throws ComException;

    /**
     * 
     */
    DictionaryEntry getEntry()
        throws ComException;
}
