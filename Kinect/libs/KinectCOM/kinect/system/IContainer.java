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
 * Represents Java interface for COM interface IContainer.
 *
 *
 */
public interface IContainer extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{61D9C50C-4AAD-3539-AF82-4F36C19D77C8}";

    /**
     * 
     */
    void add(
        IComponent /*[in]*/ component)
        throws ComException;

    /**
     * 
     */
    void add_2(
        IComponent /*[in]*/ component,
        BStr /*[in]*/ name)
        throws ComException;

    /**
     * 
     */
    void remove(
        IComponent /*[in]*/ component)
        throws ComException;

    /**
     * 
     */
    _ComponentCollection getComponents()
        throws ComException;
}
