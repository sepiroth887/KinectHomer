package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass KinectData.
 */
public class KinectData extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{EBD5D333-45C4-3D51-BA8C-266572056344}");

    public KinectData()
    {
    }

    public KinectData(KinectData that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _KinectData create(ClsCtx dwClsContext) throws ComException
    {
        final _KinectDataImpl intf = new _KinectDataImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_KinectData</code> interface from IUnknown instance.
     */
    public static _KinectData queryInterface(IUnknown unknown) throws ComException
    {
        final _KinectDataImpl result = new _KinectDataImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new KinectData(this);
    }
}