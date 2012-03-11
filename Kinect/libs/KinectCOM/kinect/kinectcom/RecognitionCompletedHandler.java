package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass RecognitionCompletedHandler.
 */
public class RecognitionCompletedHandler extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{8B1B8E33-9CBB-3D10-89EC-A2310C94DF1E}");

    public RecognitionCompletedHandler()
    {
    }

    public RecognitionCompletedHandler(RecognitionCompletedHandler that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _RecognitionCompletedHandler create(ClsCtx dwClsContext) throws ComException
    {
        final _RecognitionCompletedHandlerImpl intf = new _RecognitionCompletedHandlerImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_RecognitionCompletedHandler</code> interface from IUnknown instance.
     */
    public static _RecognitionCompletedHandler queryInterface(IUnknown unknown) throws ComException
    {
        final _RecognitionCompletedHandlerImpl result = new _RecognitionCompletedHandlerImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new RecognitionCompletedHandler(this);
    }
}