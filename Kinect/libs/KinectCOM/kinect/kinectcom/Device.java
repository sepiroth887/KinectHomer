package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Device.
 */
public class Device extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{4E7A133A-FFA8-306E-BA59-8ACD96B6B122}");

    public Device()
    {
    }

    public Device(Device that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _DeviceImpl create(ClsCtx dwClsContext) throws ComException
    {
        final _DeviceImpl intf = new _DeviceImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_DeviceImpl</code> interface from IUnknown instance.
     */
    public static _DeviceImpl queryInterface(IUnknown unknown) throws ComException
    {
        final _DeviceImpl result = new _DeviceImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Device(this);
    }
}