package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Photo.
 */
public class Photo extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{06D68AEF-D926-3171-B94D-39E119F6F32D}");

    public Photo()
    {
    }

    public Photo(Photo that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Photo create(ClsCtx dwClsContext) throws ComException
    {
        final _PhotoImpl intf = new _PhotoImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Photo</code> interface from IUnknown instance.
     */
    public static _Photo queryInterface(IUnknown unknown) throws ComException
    {
        final _PhotoImpl result = new _PhotoImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Photo(this);
    }
}