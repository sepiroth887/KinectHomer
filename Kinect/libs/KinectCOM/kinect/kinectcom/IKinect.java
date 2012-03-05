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
import kinect.mscorlib.*;
import kinect.mscorlib.impl.*;
import kinect.stdole.*;
import kinect.stdole.impl.*;

/**
 * Represents Java interface for COM interface IKinect.
 *
 *
 */
public interface IKinect extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{EBCEF8A1-A0AC-30EE-B953-F7CAA656405B}";

    /**
     * 
     */
    void init()
        throws ComException;

    /**
     * 
     */
    void uninit()
        throws ComException;

    /**
     * 
     */
    void learnUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
        throws ComException;

    /**
     * 
     */
    VariantBool startTracking(
        Int32 /*[in]*/ skeletonID)
        throws ComException;

    /**
     * 
     */
    void stopTracking(
        Int32 /*[in]*/ skeletonID)
        throws ComException;

    /**
     * 
     */
    void gestureRecordCompleted(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
        throws ComException;

    /**
     * 
     */
    void recordingCountdownEvent(
        Int32 /*[in]*/ p)
        throws ComException;

    /**
     * 
     */
    void gestureRecognitionCompleted(
        BStr /*[in]*/ Gesture)
        throws ComException;

    /**
     * 
     */
    void recordGesture(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
        throws ComException;

    /**
     * 
     */
    void recognizeGesture(
        BStr /*[in]*/ ctxt)
        throws ComException;

    /**
     * 
     */
    void stopRecGesture()
        throws ComException;

    /**
     * 
     */
    void onAddOnGestureValueChange(
        SingleFloat /*[in]*/ value)
        throws ComException;

    /**
     * 
     */
    void contextSelected(
        BStr /*[in]*/ ctxt)
        throws ComException;

    /**
     * 
     */
    void storeGestures()
        throws ComException;

    /**
     * 
     */
    IEnumerable loadGestures()
        throws ComException;

    /**
     * 
     */
    void setTrackingStrategy(
        Int32 /*[in]*/ strat)
        throws ComException;

    /**
     * 
     */
    void trackingStarted(
        Int32 /*[in]*/ matchingSkeleton)
        throws ComException;

    /**
     * 
     */
    void trackingStopped(
        Int32 /*[in]*/ matchingSkeleton)
        throws ComException;

    /**
     * 
     */
    BStr getObjects()
        throws ComException;

    /**
     * 
     */
    void userDetected(
        _User /*[in]*/ User)
        throws ComException;

    /**
     * 
     */
    void userLost(
        _User /*[in]*/ User)
        throws ComException;

    /**
     * 
     */
    void setDefaultHand(
        VariantBool /*[in]*/ def)
        throws ComException;
}
