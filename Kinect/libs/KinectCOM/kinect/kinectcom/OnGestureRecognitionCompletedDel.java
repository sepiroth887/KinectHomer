package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnGestureRecognitionCompletedDel.
 */
public class OnGestureRecognitionCompletedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{85186985-0DC4-3CDA-85AF-334266086265}");

    public OnGestureRecognitionCompletedDel()
    {
    }

    public OnGestureRecognitionCompletedDel(OnGestureRecognitionCompletedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnGestureRecognitionCompletedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnGestureRecognitionCompletedDelImpl intf = new _OnGestureRecognitionCompletedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnGestureRecognitionCompletedDel</code> interface from IUnknown instance.
     */
    public static _OnGestureRecognitionCompletedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnGestureRecognitionCompletedDelImpl result = new _OnGestureRecognitionCompletedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnGestureRecognitionCompletedDel(this);
    }
}