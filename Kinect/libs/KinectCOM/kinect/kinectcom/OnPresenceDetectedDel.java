package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnPresenceDetectedDel.
 */
public class OnPresenceDetectedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{1D164B1F-096D-3840-ABBF-634346FEE0D4}");

    public OnPresenceDetectedDel()
    {
    }

    public OnPresenceDetectedDel(OnPresenceDetectedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnPresenceDetectedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnPresenceDetectedDelImpl intf = new _OnPresenceDetectedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnPresenceDetectedDel</code> interface from IUnknown instance.
     */
    public static _OnPresenceDetectedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnPresenceDetectedDelImpl result = new _OnPresenceDetectedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnPresenceDetectedDel(this);
    }
}