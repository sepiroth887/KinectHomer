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
 * Represents Java interface for COM interface ICollection.
 *
 *
 */
public interface ICollection extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{DE8DB6F8-D101-3A92-8D1C-E72E5F10E992}";

    /**
     * 
     */
    void copyTo(
        _Array /*[in]*/ Array,
        Int32 /*[in]*/ index)
        throws ComException;

    /**
     * 
     */
    Int32 getCount()
        throws ComException;

    /**
     * 
     */
    Variant getSyncRoot()
        throws ComException;

    /**
     * 
     */
    VariantBool getIsSynchronized()
        throws ComException;
}
