package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass FaceAPI.
 */
public class FaceAPI extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{895CC7F0-F145-37EA-B60D-F9706899710A}");

    public FaceAPI()
    {
    }

    public FaceAPI(FaceAPI that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _FaceAPI create(ClsCtx dwClsContext) throws ComException
    {
        final _FaceAPIImpl intf = new _FaceAPIImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_FaceAPI</code> interface from IUnknown instance.
     */
    public static _FaceAPI queryInterface(IUnknown unknown) throws ComException
    {
        final _FaceAPIImpl result = new _FaceAPIImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new FaceAPI(this);
    }
}