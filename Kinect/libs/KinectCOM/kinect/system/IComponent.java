package kinect.system;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.*;
import kinect.mscorlib.impl.*;
import kinect.system.impl.*;

/**
 * Represents Java interface for COM interface IComponent.
 *
 *
 */
public interface IComponent extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{B86E59F2-F1E2-389D-B5F1-C55307C8106E}";

    /**
     * 
     */
    void add_Disposed(
        _EventHandler /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    void remove_Disposed(
        _EventHandler /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    ISite getSite()
        throws ComException;

    /**
     * 
     */
    void setSite(
        ISite /*[in]*/ pRetVal)
        throws ComException;
}
