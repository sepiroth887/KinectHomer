package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Point.
 */
public class Point extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{4090F8CB-E6D9-3CE3-8855-C86684793591}");

    public Point()
    {
    }

    public Point(Point that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Point create(ClsCtx dwClsContext) throws ComException
    {
        final _PointImpl intf = new _PointImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Point</code> interface from IUnknown instance.
     */
    public static _Point queryInterface(IUnknown unknown) throws ComException
    {
        final _PointImpl result = new _PointImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Point(this);
    }
}