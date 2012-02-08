package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnPresenceLostDel.
 */
public class OnPresenceLostDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{19305D32-8BC3-3981-8F8A-E069F7ACE7E7}");

    public OnPresenceLostDel()
    {
    }

    public OnPresenceLostDel(OnPresenceLostDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnPresenceLostDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnPresenceLostDelImpl intf = new _OnPresenceLostDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnPresenceLostDel</code> interface from IUnknown instance.
     */
    public static _OnPresenceLostDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnPresenceLostDelImpl result = new _OnPresenceLostDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnPresenceLostDel(this);
    }
}