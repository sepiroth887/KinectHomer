package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Usage.
 */
public class Usage extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{8929AD12-FF54-35F9-846D-C108CC121A51}");

    public Usage()
    {
    }

    public Usage(Usage that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Usage create(ClsCtx dwClsContext) throws ComException
    {
        final _UsageImpl intf = new _UsageImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Usage</code> interface from IUnknown instance.
     */
    public static _Usage queryInterface(IUnknown unknown) throws ComException
    {
        final _UsageImpl result = new _UsageImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Usage(this);
    }
}