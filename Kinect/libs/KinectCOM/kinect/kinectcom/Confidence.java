package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Confidence.
 */
public class Confidence extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{96F544A9-56A5-33DA-9F7F-A8628A8A341F}");

    public Confidence()
    {
    }

    public Confidence(Confidence that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Confidence create(ClsCtx dwClsContext) throws ComException
    {
        final _ConfidenceImpl intf = new _ConfidenceImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Confidence</code> interface from IUnknown instance.
     */
    public static _Confidence queryInterface(IUnknown unknown) throws ComException
    {
        final _ConfidenceImpl result = new _ConfidenceImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Confidence(this);
    }
}