package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnVoiceCommandDetectedDel.
 */
public class OnVoiceCommandDetectedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{5409CEC2-38AD-374D-8EE4-29DF14CF8718}");

    public OnVoiceCommandDetectedDel()
    {
    }

    public OnVoiceCommandDetectedDel(OnVoiceCommandDetectedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnVoiceCommandDetectedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnVoiceCommandDetectedDelImpl intf = new _OnVoiceCommandDetectedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnVoiceCommandDetectedDel</code> interface from IUnknown instance.
     */
    public static _OnVoiceCommandDetectedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnVoiceCommandDetectedDelImpl result = new _OnVoiceCommandDetectedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnVoiceCommandDetectedDel(this);
    }
}