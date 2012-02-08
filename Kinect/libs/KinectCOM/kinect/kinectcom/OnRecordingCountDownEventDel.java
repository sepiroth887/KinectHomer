package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnRecordingCountDownEventDel.
 */
public class OnRecordingCountDownEventDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{3A0EA31A-FB25-39CC-A327-8FBCC2CBEA30}");

    public OnRecordingCountDownEventDel()
    {
    }

    public OnRecordingCountDownEventDel(OnRecordingCountDownEventDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnRecordingCountDownEventDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnRecordingCountDownEventDelImpl intf = new _OnRecordingCountDownEventDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnRecordingCountDownEventDel</code> interface from IUnknown instance.
     */
    public static _OnRecordingCountDownEventDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnRecordingCountDownEventDelImpl result = new _OnRecordingCountDownEventDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnRecordingCountDownEventDel(this);
    }
}