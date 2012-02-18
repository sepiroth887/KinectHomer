package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnUserLostDel.
 */
public class OnUserLostDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{4E482EE3-A29B-3C34-A8E9-04CA19870DEA}");

    public OnUserLostDel()
    {
    }

    public OnUserLostDel(OnUserLostDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnUserLostDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnUserLostDelImpl intf = new _OnUserLostDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnUserLostDel</code> interface from IUnknown instance.
     */
    public static _OnUserLostDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnUserLostDelImpl result = new _OnUserLostDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnUserLostDel(this);
    }
}