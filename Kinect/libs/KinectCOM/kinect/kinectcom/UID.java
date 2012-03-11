package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass UID.
 */
public class UID extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{25A06581-35B1-3C86-B0A1-1EB3ED373103}");

    public UID()
    {
    }

    public UID(UID that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _UID create(ClsCtx dwClsContext) throws ComException
    {
        final _UIDImpl intf = new _UIDImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_UID</code> interface from IUnknown instance.
     */
    public static _UID queryInterface(IUnknown unknown) throws ComException
    {
        final _UIDImpl result = new _UIDImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new UID(this);
    }
}