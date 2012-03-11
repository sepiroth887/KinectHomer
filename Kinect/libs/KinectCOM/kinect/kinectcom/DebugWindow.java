package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;
import kinect.system.impl.*;
import kinect.system_windows_forms.impl.*;

/**
 * Represents COM coclass DebugWindow.
 */
public class DebugWindow extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{6264633D-5627-39DB-B27F-91D6A18A1CC2}");

    public DebugWindow()
    {
    }

    public DebugWindow(DebugWindow that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _DebugWindow create(ClsCtx dwClsContext) throws ComException
    {
        final _DebugWindowImpl intf = new _DebugWindowImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_DebugWindow</code> interface from IUnknown instance.
     */
    public static _DebugWindow queryInterface(IUnknown unknown) throws ComException
    {
        final _DebugWindowImpl result = new _DebugWindowImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new DebugWindow(this);
    }
}