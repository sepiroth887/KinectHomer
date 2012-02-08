package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnGestureRecordCompletedDel.
 */
public class OnGestureRecordCompletedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{0F811B69-72C6-3A73-A244-56A3925696AA}");

    public OnGestureRecordCompletedDel()
    {
    }

    public OnGestureRecordCompletedDel(OnGestureRecordCompletedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnGestureRecordCompletedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnGestureRecordCompletedDelImpl intf = new _OnGestureRecordCompletedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnGestureRecordCompletedDel</code> interface from IUnknown instance.
     */
    public static _OnGestureRecordCompletedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnGestureRecordCompletedDelImpl result = new _OnGestureRecordCompletedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnGestureRecordCompletedDel(this);
    }
}