package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Recognizer.
 */
public class Recognizer extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{24C0FAA8-F4B2-30B5-88FD-3E4465D42337}");

    public Recognizer()
    {
    }

    public Recognizer(Recognizer that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Recognizer create(ClsCtx dwClsContext) throws ComException
    {
        final _RecognizerImpl intf = new _RecognizerImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Recognizer</code> interface from IUnknown instance.
     */
    public static _Recognizer queryInterface(IUnknown unknown) throws ComException
    {
        final _RecognizerImpl result = new _RecognizerImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Recognizer(this);
    }
}