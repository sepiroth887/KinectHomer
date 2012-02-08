package kinect.kinectcom.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.kinectcom.*;

/**
 * Represents COM dispinterface ISpeechEvents.
 */
public class ISpeechEventsImpl extends IDispatchImpl
    implements ISpeechEvents
{
    public static final String INTERFACE_IDENTIFIER = "{387C8328-F599-34C2-BA63-CEBEA49E5547}";
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public ISpeechEventsImpl()
    {
    }

    protected ISpeechEventsImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public ISpeechEventsImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public ISpeechEventsImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public ISpeechEventsImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void onSpeechRecognized(
        BStr /*[in]*/ command,
        Int32 /*[in]*/ confidence)
    {

        Parameter[] parameters = new Parameter[] {
                command == null ? (Parameter)PTR_NULL : command,
                confidence
            };

         Automation.invokeDispatch(this, "OnSpeechRecognized", parameters, void.class);
    }

    /**
     * 
     */
    public void onSpeechDetected()
    {

        Parameter[] parameters = new Parameter[0];

         Automation.invokeDispatch(this, "OnSpeechDetected", parameters, void.class);
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new ISpeechEventsImpl(this);
    }
}
