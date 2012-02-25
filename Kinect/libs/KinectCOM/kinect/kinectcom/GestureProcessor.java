package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass GestureProcessor.
 */
public class GestureProcessor extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{077E48B2-66AB-3A3C-BE1D-F35589F23E5C}");

    public GestureProcessor()
    {
    }

    public GestureProcessor(GestureProcessor that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _GestureProcessor create(ClsCtx dwClsContext) throws ComException
    {
        final _GestureProcessorImpl intf = new _GestureProcessorImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_GestureProcessor</code> interface from IUnknown instance.
     */
    public static _GestureProcessor queryInterface(IUnknown unknown) throws ComException
    {
        final _GestureProcessorImpl result = new _GestureProcessorImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new GestureProcessor(this);
    }
}