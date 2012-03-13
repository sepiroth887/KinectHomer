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
 * Represents Java interface for COM interface IDevice.
 *
 *
 */
public interface IDevice extends IDispatch
{
    public static final String INTERFACE_IDENTIFIER = "{92A28D11-004C-3146-90A8-AF8E242E41B6}";

    public static final int DISPID_setContext = 9;
    public static final int DISPID_speechRecognition = 10;
    public static final int DISPID_learnUser = 11;
    public static final int DISPID_updateUser = 12;
    public static final int DISPID_setUserToSkeleton = 13;
    public static final int DISPID_incAngle = 14;
    public static final int DISPID_decAngle = 15;
    public static final int DISPID_startTracking = 16;
    public static final int DISPID_recordGesture = 17;
    public static final int DISPID_recognizeGesture = 18;
    public static final int DISPID_stopGestureRecognition = 19;
    public static final int DISPID_storeGestures = 20;
    public static final int DISPID_loadGestures = 21;
    public static final int DISPID_getObjects = 22;
    public static final int DISPID_setDefaultHand = 23;
    public static final int DISPID_loadUsers = 24;
    public static final int DISPID_addUser = 25;
    public static final int DISPID_delUser = 26;
    public static final int DISPID_setNewObjectContext = 27;
    public static final int DISPID_init = 42;
    public static final int DISPID_uninit = 43;


    /**
     * 
     */
    void setContext(
        BStr /*[in]*/ contextID);

    /**
     * 
     */
    void speechRecognition(
        VariantBool /*[in]*/ on);

    /**
     * 
     */
    VariantBool learnUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    VariantBool updateUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    VariantBool setUserToSkeleton(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    void incAngle();

    /**
     * 
     */
    void decAngle();

    /**
     * 
     */
    VariantBool startTracking(
        Int32 /*[in]*/ skeletonID);

    /**
     * 
     */
    void recordGesture(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt);

    /**
     * 
     */
    void recognizeGesture(
        BStr /*[in]*/ ctxt);

    /**
     * 
     */
    void stopGestureRecognition();

    /**
     * 
     */
    void storeGestures();

    /**
     * 
     */
    BStr loadGestures();

    /**
     * 
     */
    BStr getObjects();

    /**
     * 
     */
    void setDefaultHand(
        VariantBool /*[in]*/ def);

    /**
     * 
     */
    BStr loadUsers();

    /**
     * 
     */
    void addUser(
        BStr /*[in]*/ User);

    /**
     * 
     */
    void delUser(
        BStr /*[in]*/ User);

    /**
     * 
     */
    void setNewObjectContext(
        BStr /*[in]*/ context);

    /**
     * 
     */
    VariantBool init();

    /**
     * 
     */
    void uninit();
}
