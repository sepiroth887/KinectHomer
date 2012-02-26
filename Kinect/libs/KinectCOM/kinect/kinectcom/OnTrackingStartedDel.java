package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnTrackingStartedDel.
 */
public class OnTrackingStartedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{298ABCF7-3358-31A4-A572-B1D08D136BFD}");

    public OnTrackingStartedDel()
    {
    }

    public OnTrackingStartedDel(OnTrackingStartedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnTrackingStartedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnTrackingStartedDelImpl intf = new _OnTrackingStartedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnTrackingStartedDel</code> interface from IUnknown instance.
     */
    public static _OnTrackingStartedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnTrackingStartedDelImpl result = new _OnTrackingStartedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnTrackingStartedDel(this);
    }
}