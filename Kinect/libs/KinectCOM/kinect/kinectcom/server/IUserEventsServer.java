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
 * Adapter for server implementation of IUserEvents
 */
public class IUserEventsServer extends IDispatchServer
    implements IUserEvents
{
    public IUserEventsServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(IUserEvents.class);
    }

    /**
     * 
     */
    public void onPresenceDetected(
        Int32 /*[in]*/ skeletonID)
    {
    }

    /**
     * 
     */
    public void onUserFound(
        BStr /*[in]*/ user,
        SingleFloat /*[in]*/ confidence,
        Int32 /*[in]*/ skeletonID)
    {
    }

    /**
     * 
     */
    public void onUserLost(
        BStr /*[in]*/ user)
    {
    }

    /**
     * 
     */
    public void onPresenceLost(
        Int32 /*[in]*/ skeletonID)
    {
    }

    /**
     * 
     */
    public void onGestureRecognitionCompleted(
        BStr /*[in]*/ gestureName)
    {
    }

    /**
     * 
     */
    public void onGestureRecordCompleted(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
    {
    }

    /**
     * 
     */
    public void onRecordingCountDownEvent(
        Int32 /*[in]*/ time)
    {
    }

    /**
     * 
     */
    public void onContextSelected(
        BStr /*[in]*/ ctxt)
    {
    }

    /**
     * 
     */
    public void onVoiceCommandDetected(
        BStr /*[in]*/ command)
    {
    }

    /**
     * 
     */
    public void onAddOnGestureValueChange(
        SingleFloat /*[in]*/ value)
    {
    }

}