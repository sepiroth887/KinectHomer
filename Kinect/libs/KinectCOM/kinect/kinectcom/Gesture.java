package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Gesture.
 */
public class Gesture extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{147960A6-5D47-3D58-86A2-332E88DFD0DB}");

    public Gesture()
    {
    }

    public Gesture(Gesture that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Gesture create(ClsCtx dwClsContext) throws ComException
    {
        final _GestureImpl intf = new _GestureImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Gesture</code> interface from IUnknown instance.
     */
    public static _Gesture queryInterface(IUnknown unknown) throws ComException
    {
        final _GestureImpl result = new _GestureImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Gesture(this);
    }
}