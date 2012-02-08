package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnAddonGestureValueChangeDel.
 */
public class OnAddonGestureValueChangeDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{5E7C25C3-A89A-3338-B1CF-738E35CEA09D}");

    public OnAddonGestureValueChangeDel()
    {
    }

    public OnAddonGestureValueChangeDel(OnAddonGestureValueChangeDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnAddonGestureValueChangeDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnAddonGestureValueChangeDelImpl intf = new _OnAddonGestureValueChangeDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnAddonGestureValueChangeDel</code> interface from IUnknown instance.
     */
    public static _OnAddonGestureValueChangeDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnAddonGestureValueChangeDelImpl result = new _OnAddonGestureValueChangeDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnAddonGestureValueChangeDel(this);
    }
}