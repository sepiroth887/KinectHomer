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
 * Represents Java interface for COM interface IUserEvents.
 *
 *
 */
public interface IUserEvents extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{A4EF185E-AD46-3E6F-BD8F-D5B2486970D5}";

    public static final int DISPID_onPresenceDetected = 1;
    public static final int DISPID_onUserFound = 2;
    public static final int DISPID_onUserLost = 3;
    public static final int DISPID_onPresenceLost = 4;
    public static final int DISPID_onGestureRecognitionCompleted = 6;
    public static final int DISPID_onGestureRecordCompleted = 7;
    public static final int DISPID_onRecordingCountDownEvent = 8;
    public static final int DISPID_onContextSelected = 9;
    public static final int DISPID_onVoiceCommandDetected = 10;
    public static final int DISPID_onAddonGestureValueChange = 11;


    /**
     * 
     */
    void onPresenceDetected(
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    void onUserFound(
        BStr /*[in]*/ user,
        SingleFloat /*[in]*/ confidence,
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    void onUserLost(
        BStr /*[in]*/ user);

    /**
     * 
     */
    void onPresenceLost(
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    void onGestureRecognitionCompleted(
        BStr /*[in]*/ gestureName);

    /**
     * 
     */
    void onGestureRecordCompleted(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt);

    /**
     * 
     */
    void onRecordingCountDownEvent(
        Int32 /*[in]*/ time);

    /**
     * 
     */
    void onContextSelected(
        BStr /*[in]*/ ctxt);

    /**
     * 
     */
    void onVoiceCommandDetected(
        BStr /*[in]*/ command);

    /**
     * 
     */
    void onAddonGestureValueChange(
        SingleFloat /*[in]*/ value);
}
