package kinect.system_windows_forms;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import kinect.system_windows_forms.impl.*;

/**
 * Represents Java interface for COM interface IWin32Window.
 *
 *
 */
public interface IWin32Window extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{458AB8A2-A1EA-4D7B-8EBE-DEE5D3D9442C}";

    /**
     * 
     */
    Int32 getHandle()
        throws ComException;
}
