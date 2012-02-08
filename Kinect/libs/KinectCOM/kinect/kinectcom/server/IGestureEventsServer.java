package kinect.kinectcom.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.kinectcom.*;
import kinect.kinectcom.impl.*;

/**
 * Adapter for server implementation of IGestureEvents
 */
public class IGestureEventsServer extends IDispatchServer
    implements IGestureEvents
{
    public IGestureEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(IGestureEvents.class);
    }

    /**
     * 
     */
    public void onGestureRecognized(
        Int32 /*[in]*/ gestureID)
    {
    }

    /**
     * 
     */
    public void onContextChanged(
        Int32 /*[in]*/ contextID)
    {
    }

}