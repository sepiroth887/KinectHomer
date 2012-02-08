package kinect.kinectcom.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.kinectcom.*;

/**
 * Represents COM dispinterface _Device.
 */
public class _DeviceImpl extends IDispatchImpl
    implements _Device
{
    public static final String INTERFACE_IDENTIFIER = "{946AD531-061A-3493-87A2-28F0B5F7FDD9}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public _DeviceImpl()
    {
    }

    protected _DeviceImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public _DeviceImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public _DeviceImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public _DeviceImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void userRecognition(
        VariantBool /*[in]*/ on)
    {

        Parameter[] parameters = new Parameter[] {
                on
            };

         Automation.invokeDispatch(this, "userRecognition", parameters, void.class);
    }

    /**
     * 
     */
    public void setContext(
        BStr /*[in]*/ contextID)
    {

        Parameter[] parameters = new Parameter[] {
                contextID == null ? (Parameter)PTR_NULL : contextID
            };

         Automation.invokeDispatch(this, "setContext", parameters, void.class);
    }

    /**
     * 
     */
    public void speechRecognition(
        VariantBool /*[in]*/ on)
    {

        Parameter[] parameters = new Parameter[] {
                on
            };

         Automation.invokeDispatch(this, "speechRecognition", parameters, void.class);
    }

    /**
     * 
     */
    public VariantBool learnUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                skeletonID
            };

         Object result = Automation.invokeDispatch(this, "learnUser", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public VariantBool updateUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                skeletonID
            };

         Object result = Automation.invokeDispatch(this, "updateUser", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public VariantBool setUserToSkeleton(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                skeletonID
            };

         Object result = Automation.invokeDispatch(this, "setUserToSkeleton", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public void incAngle()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "incAngle", parameters, void.class);
    }

    /**
     * 
     */
    public void decAngle()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "decAngle", parameters, void.class);
    }

    /**
     * 
     */
    public VariantBool startTracking(
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                skeletonID
            };

         Object result = Automation.invokeDispatch(this, "startTracking", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public void recordGesture(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
    {

        Parameter[] parameters = new Parameter[] {
                gestureName == null ? (Parameter)PTR_NULL : gestureName,
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            };

         Automation.invokeDispatch(this, "recordGesture", parameters, void.class);
    }

    /**
     * 
     */
    public void recognizeGesture(
        BStr /*[in]*/ ctxt)
    {

        Parameter[] parameters = new Parameter[] {
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            };

         Automation.invokeDispatch(this, "recognizeGesture", parameters, void.class);
    }

    /**
     * 
     */
    public void stopGestureRecognition()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "stopGestureRecognition", parameters, void.class);
    }

    /**
     * 
     */
    public VariantBool init()
    {

        Parameter[] parameters = new Parameter[0];

         Object result = Automation.invokeDispatch(this, "init", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public void uninit()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "uninit", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new _DeviceImpl(this);
    }
}
