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
    public static final String INTERFACE_IDENTIFIER = "{6EEC98DB-A3C9-37DF-B5A8-BB6140A71D56}";

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
    void updateFace()
        throws ComException;

    /**
     * 
     */
    void updateSkeletons(
        IDictionary /*[in]*/ points,
        _ArrayList /*[in]*/ users)
        throws ComException;

    /**
     * 
     */
    void learnUser(
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
    void userDetected(
        _UserFeature /*[in]*/ user)
        throws ComException;

    /**
     * 
     */
    void presenceDetected(
        Int32 /*[in]*/ skeletonID)
        throws ComException;

    /**
     * 
     */
    void userLost(
        _UserFeature /*[in]*/ user)
        throws ComException;

    /**
     * 
     */
    void presenceLost(
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
}
