package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass FaceRestAPI.
 */
public class FaceRestAPI extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{9A3CE151-7786-36A4-A77F-F438686D4D91}");

    public FaceRestAPI()
    {
    }

    public FaceRestAPI(FaceRestAPI that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _FaceRestAPI create(ClsCtx dwClsContext) throws ComException
    {
        final _FaceRestAPIImpl intf = new _FaceRestAPIImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_FaceRestAPI</code> interface from IUnknown instance.
     */
    public static _FaceRestAPI queryInterface(IUnknown unknown) throws ComException
    {
        final _FaceRestAPIImpl result = new _FaceRestAPIImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new FaceRestAPI(this);
    }
}