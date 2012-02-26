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
 * Represents COM dispinterface IDevice.
 */
public class IDeviceImpl extends IDispatchImpl
    implements IDevice
{
    public static final String INTERFACE_IDENTIFIER = "{ABF23968-C7BF-3ED0-B967-A7C0A776A3EC}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IDeviceImpl()
    {
    }

    protected IDeviceImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IDeviceImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IDeviceImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IDeviceImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
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

         Automation.invokeDispatch(this, "UserRecognition", parameters, void.class);
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

         Automation.invokeDispatch(this, "SetContext", parameters, void.class);
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

         Automation.invokeDispatch(this, "SpeechRecognition", parameters, void.class);
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

         Object result = Automation.invokeDispatch(this, "LearnUser", parameters, VariantBool.class);
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

         Object result = Automation.invokeDispatch(this, "UpdateUser", parameters, VariantBool.class);
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

         Object result = Automation.invokeDispatch(this, "SetUserToSkeleton", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public void incAngle()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "IncAngle", parameters, void.class);
    }

    /**
     * 
     */
    public void decAngle()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "DecAngle", parameters, void.class);
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

         Object result = Automation.invokeDispatch(this, "StartTracking", parameters, VariantBool.class);
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

         Automation.invokeDispatch(this, "RecordGesture", parameters, void.class);
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

         Automation.invokeDispatch(this, "RecognizeGesture", parameters, void.class);
    }

    /**
     * 
     */
    public void stopGestureRecognition()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "StopGestureRecognition", parameters, void.class);
    }

    /**
     * 
     */
    public void storeGestures()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "StoreGestures", parameters, void.class);
    }

    /**
     * 
     */
    public BStr loadGestures()
    {

        Parameter[] parameters = new Parameter[0];

         Object result = Automation.invokeDispatch(this, "LoadGestures", parameters, BStr.class);
        return (BStr) result;
    }

    /**
     * 
     */
    public BStr getObjects()
    {

        Parameter[] parameters = new Parameter[0];

         Object result = Automation.invokeDispatch(this, "getObjects", parameters, BStr.class);
        return (BStr) result;
    }

    /**
     * 
     */
    public VariantBool init()
    {

        Parameter[] parameters = new Parameter[0];

         Object result = Automation.invokeDispatch(this, "Init", parameters, VariantBool.class);
        return (VariantBool) result;
    }

    /**
     * 
     */
    public void uninit()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "Uninit", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IDeviceImpl(this);
    }
}
