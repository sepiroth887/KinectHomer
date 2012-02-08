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
 * Adapter for server implementation of ISpeechEvents
 */
public class ISpeechEventsServer extends IDispatchServer
    implements ISpeechEvents
{
    public ISpeechEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(ISpeechEvents.class);
    }

    /**
     * 
     */
    public void onSpeechRecognized(
        BStr /*[in]*/ command,
        Int32 /*[in]*/ confidence)
    {
    }

    /**
     * 
     */
    public void onSpeechDetected()
    {
    }

}