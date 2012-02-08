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
 * Represents COM dispinterface IGestureEvents.
 */
public class IGestureEventsImpl extends IDispatchImpl
    implements IGestureEvents
{
    public static final String INTERFACE_IDENTIFIER = "{26C07B69-84C2-320F-AB4A-C5294C2EC14D}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IGestureEventsImpl()
    {
    }

    protected IGestureEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IGestureEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IGestureEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IGestureEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void onGestureRecognized(
        Int32 /*[in]*/ gestureID)
    {

        Parameter[] parameters = new Parameter[] {
                gestureID
            };

         Automation.invokeDispatch(this, "OnGestureRecognized", parameters, void.class);
    }

    /**
     * 
     */
    public void onContextChanged(
        Int32 /*[in]*/ contextID)
    {

        Parameter[] parameters = new Parameter[] {
                contextID
            };

         Automation.invokeDispatch(this, "OnContextChanged", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IGestureEventsImpl(this);
    }
}
