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
 * Represents COM dispinterface IUserEvents.
 */
public class IUserEventsImpl extends IDispatchImpl
    implements IUserEvents
{
    public static final String INTERFACE_IDENTIFIER = "{A4EF185E-AD46-3E6F-BD8F-D5B2486970D5}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IUserEventsImpl()
    {
    }

    protected IUserEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IUserEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IUserEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IUserEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void onPresenceDetected(
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                skeletonID
            };

         Automation.invokeDispatch(this, "OnPresenceDetected", parameters, void.class);
    }

    /**
     * 
     */
    public void onUserFound(
        BStr /*[in]*/ user,
        SingleFloat /*[in]*/ confidence,
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                user == null ? (Parameter)PTR_NULL : user,
                confidence,
                skeletonID
            };

         Automation.invokeDispatch(this, "OnUserFound", parameters, void.class);
    }

    /**
     * 
     */
    public void onUserLost(
        BStr /*[in]*/ user)
    {

        Parameter[] parameters = new Parameter[] {
                user == null ? (Parameter)PTR_NULL : user
            };

         Automation.invokeDispatch(this, "OnUserLost", parameters, void.class);
    }

    /**
     * 
     */
    public void onPresenceLost(
        Int32 /*[in]*/ skeletonID)
    {

        Parameter[] parameters = new Parameter[] {
                skeletonID
            };

         Automation.invokeDispatch(this, "OnPresenceLost", parameters, void.class);
    }

    /**
     * 
     */
    public void onGestureRecognitionCompleted(
        BStr /*[in]*/ gestureName)
    {

        Parameter[] parameters = new Parameter[] {
                gestureName == null ? (Parameter)PTR_NULL : gestureName
            };

         Automation.invokeDispatch(this, "OnGestureRecognitionCompleted", parameters, void.class);
    }

    /**
     * 
     */
    public void onGestureRecordCompleted(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
    {

        Parameter[] parameters = new Parameter[] {
                gestureName == null ? (Parameter)PTR_NULL : gestureName,
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            };

         Automation.invokeDispatch(this, "OnGestureRecordCompleted", parameters, void.class);
    }

    /**
     * 
     */
    public void onRecordingCountDownEvent(
        Int32 /*[in]*/ time)
    {

        Parameter[] parameters = new Parameter[] {
                time
            };

         Automation.invokeDispatch(this, "OnRecordingCountDownEvent", parameters, void.class);
    }

    /**
     * 
     */
    public void onContextSelected(
        BStr /*[in]*/ ctxt)
    {

        Parameter[] parameters = new Parameter[] {
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            };

         Automation.invokeDispatch(this, "OnContextSelected", parameters, void.class);
    }

    /**
     * 
     */
    public void onVoiceCommandDetected(
        BStr /*[in]*/ command)
    {

        Parameter[] parameters = new Parameter[] {
                command == null ? (Parameter)PTR_NULL : command
            };

         Automation.invokeDispatch(this, "OnVoiceCommandDetected", parameters, void.class);
    }

    /**
     * 
     */
    public void onAddOnGestureValueChange(
        SingleFloat /*[in]*/ value)
    {

        Parameter[] parameters = new Parameter[] {
                value
            };

         Automation.invokeDispatch(this, "OnAddOnGestureValueChange", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IUserEventsImpl(this);
    }
}
