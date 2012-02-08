package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnUserFoundDel.
 */
public class OnUserFoundDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{094BC31B-2FCE-36F7-B695-260D3061D042}");

    public OnUserFoundDel()
    {
    }

    public OnUserFoundDel(OnUserFoundDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnUserFoundDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnUserFoundDelImpl intf = new _OnUserFoundDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnUserFoundDel</code> interface from IUnknown instance.
     */
    public static _OnUserFoundDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnUserFoundDelImpl result = new _OnUserFoundDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnUserFoundDel(this);
    }
}