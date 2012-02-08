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
 * Represents Java interface for COM interface ISpeechEvents.
 *
 *
 */
public interface ISpeechEvents extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{387C8328-F599-34C2-BA63-CEBEA49E5547}";

    public static final int DISPID_onSpeechRecognized = 4;
    public static final int DISPID_onSpeechDetected = 5;


    /**
     * 
     */
    void onSpeechRecognized(
        BStr /*[in]*/ command,
        Int32 /*[in]*/ confidence);

    /**
     * 
     */
    void onSpeechDetected();
}
