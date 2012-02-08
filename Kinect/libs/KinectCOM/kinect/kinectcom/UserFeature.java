package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass UserFeature.
 */
public class UserFeature extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{1188BB07-811A-38D0-9EEC-D1CCD125CAB8}");

    public UserFeature()
    {
    }

    public UserFeature(UserFeature that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _UserFeature create(ClsCtx dwClsContext) throws ComException
    {
        final _UserFeatureImpl intf = new _UserFeatureImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_UserFeature</code> interface from IUnknown instance.
     */
    public static _UserFeature queryInterface(IUnknown unknown) throws ComException
    {
        final _UserFeatureImpl result = new _UserFeatureImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new UserFeature(this);
    }
}