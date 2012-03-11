package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass UserStatus.
 */
public class UserStatus extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{42914351-C257-3137-86FF-6E230CCE4522}");

    public UserStatus()
    {
    }

    public UserStatus(UserStatus that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _UserStatus create(ClsCtx dwClsContext) throws ComException
    {
        final _UserStatusImpl intf = new _UserStatusImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_UserStatus</code> interface from IUnknown instance.
     */
    public static _UserStatus queryInterface(IUnknown unknown) throws ComException
    {
        final _UserStatusImpl result = new _UserStatusImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new UserStatus(this);
    }
}