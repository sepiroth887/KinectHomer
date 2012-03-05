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
 * Adapter for server implementation of IDevice
 */
public class IDeviceServer extends IDispatchServer
    implements IDevice
{
    public IDeviceServer(CoClassMetaInfo classImpl)
    {
        super(classImpl);

        registerMethods(IDevice.class);
    }

    /**
     * 
     */
    public void userRecognition(
        VariantBool /*[in]*/ on)
    {
    }

    /**
     * 
     */
    public void setContext(
        BStr /*[in]*/ contextID)
    {
    }

    /**
     * 
     */
    public void speechRecognition(
        VariantBool /*[in]*/ on)
    {
    }

    /**
     * 
     */
    public VariantBool learnUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public VariantBool updateUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public VariantBool setUserToSkeleton(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public void incAngle()
    {
    }

    /**
     * 
     */
    public void decAngle()
    {
    }

    /**
     * 
     */
    public VariantBool startTracking(
        Int32 /*[in]*/ skeletonID)
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public void recordGesture(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
    {
    }

    /**
     * 
     */
    public void recognizeGesture(
        BStr /*[in]*/ ctxt)
    {
    }

    /**
     * 
     */
    public void stopGestureRecognition()
    {
    }

    /**
     * 
     */
    public void storeGestures()
    {
    }

    /**
     * 
     */
    public BStr loadGestures()
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public BStr getObjects()
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public void setDefaultHand(
        VariantBool /*[in]*/ def)
    {
    }

    /**
     * 
     */
    public VariantBool init()
    {
        throw new ComException(HResult.E_NOTIMPL);
    }

    /**
     * 
     */
    public void uninit()
    {
    }

}