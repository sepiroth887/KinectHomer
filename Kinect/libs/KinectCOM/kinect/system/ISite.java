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
 * Represents Java interface for COM interface ISite.
 *
 *
 */
public interface ISite extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{C4E1006A-9D98-3E96-A07E-921725135C28}";

    /**
     * 
     */
    IComponent getComponent()
        throws ComException;

    /**
     * 
     */
    IContainer getContainer()
        throws ComException;

    /**
     * 
     */
    VariantBool getDesignMode()
        throws ComException;

    /**
     * 
     */
    BStr getName()
        throws ComException;

    /**
     * 
     */
    void setName(
        BStr /*[in]*/ pRetVal)
        throws ComException;
}
