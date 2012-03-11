package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Tag.
 */
public class Tag extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{1004C859-FED4-3142-9EF3-7F81F3C3EF5A}");

    public Tag()
    {
    }

    public Tag(Tag that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Tag create(ClsCtx dwClsContext) throws ComException
    {
        final _TagImpl intf = new _TagImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Tag</code> interface from IUnknown instance.
     */
    public static _Tag queryInterface(IUnknown unknown) throws ComException
    {
        final _TagImpl result = new _TagImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Tag(this);
    }
}