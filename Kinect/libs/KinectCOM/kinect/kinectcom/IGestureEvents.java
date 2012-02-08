package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.kinectcom.impl.*;

/**
 * Represents Java interface for COM interface IGestureEvents.
 *
 *
 */
public interface IGestureEvents extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{26C07B69-84C2-320F-AB4A-C5294C2EC14D}";

    public static final int DISPID_onGestureRecognized = 6;
    public static final int DISPID_onContextChanged = 7;


    /**
     * 
     */
    void onGestureRecognized(
        Int32 /*[in]*/ gestureID);

    /**
     * 
     */
    void onContextChanged(
        Int32 /*[in]*/ contextID);
}
