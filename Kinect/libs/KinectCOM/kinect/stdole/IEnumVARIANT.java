package kinect.stdole;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import kinect.stdole.impl.*;

/**
 * Represents Java interface for COM interface IEnumVARIANT.
 *
 *
 */
public interface IEnumVARIANT extends IUnknown
{
    public static final String INTERFACE_IDENTIFIER = "{00020404-0000-0000-C000-000000000046}";

    /**
     * 
     */
    void next(
        UInt32 /*[in]*/ celt,
        Variant /*[in]*/ rgvar,
        UInt32 /*[out]*/ pceltFetched)
        throws ComException;

    /**
     * 
     */
    void skip(
        UInt32 /*[in]*/ celt)
        throws ComException;

    /**
     * 
     */
    void reset()
        throws ComException;

    /**
     * 
     */
    void clone_(
        IEnumVARIANT /*[out]*/ ppenum)
        throws ComException;
}
