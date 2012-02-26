package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnTrackingStoppedDel.
 */
public class OnTrackingStoppedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{34319329-2334-3105-81BE-CC55C22D811C}");

    public OnTrackingStoppedDel()
    {
    }

    public OnTrackingStoppedDel(OnTrackingStoppedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnTrackingStoppedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnTrackingStoppedDelImpl intf = new _OnTrackingStoppedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnTrackingStoppedDel</code> interface from IUnknown instance.
     */
    public static _OnTrackingStoppedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnTrackingStoppedDelImpl result = new _OnTrackingStoppedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnTrackingStoppedDel(this);
    }
}