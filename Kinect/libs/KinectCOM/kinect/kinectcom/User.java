package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass User.
 */
public class User extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{6179A2F3-9F44-34F5-B48A-05D9DC841090}");

    public User()
    {
    }

    public User(User that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _User create(ClsCtx dwClsContext) throws ComException
    {
        final _UserImpl intf = new _UserImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_User</code> interface from IUnknown instance.
     */
    public static _User queryInterface(IUnknown unknown) throws ComException
    {
        final _UserImpl result = new _UserImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new User(this);
    }
}