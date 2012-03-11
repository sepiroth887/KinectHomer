package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass TrainingCompletedHandler.
 */
public class TrainingCompletedHandler extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{2E423C81-AFB1-3255-98E9-6939AD143FF3}");

    public TrainingCompletedHandler()
    {
    }

    public TrainingCompletedHandler(TrainingCompletedHandler that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _TrainingCompletedHandler create(ClsCtx dwClsContext) throws ComException
    {
        final _TrainingCompletedHandlerImpl intf = new _TrainingCompletedHandlerImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_TrainingCompletedHandler</code> interface from IUnknown instance.
     */
    public static _TrainingCompletedHandler queryInterface(IUnknown unknown) throws ComException
    {
        final _TrainingCompletedHandlerImpl result = new _TrainingCompletedHandlerImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new TrainingCompletedHandler(this);
    }
}