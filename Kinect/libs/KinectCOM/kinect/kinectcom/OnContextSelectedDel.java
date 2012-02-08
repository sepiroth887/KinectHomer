package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass OnContextSelectedDel.
 */
public class OnContextSelectedDel extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{46E7AC9B-4ABE-3195-ACF6-AEFF392B94FF}");

    public OnContextSelectedDel()
    {
    }

    public OnContextSelectedDel(OnContextSelectedDel that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _OnContextSelectedDel create(ClsCtx dwClsContext) throws ComException
    {
        final _OnContextSelectedDelImpl intf = new _OnContextSelectedDelImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_OnContextSelectedDel</code> interface from IUnknown instance.
     */
    public static _OnContextSelectedDel queryInterface(IUnknown unknown) throws ComException
    {
        final _OnContextSelectedDelImpl result = new _OnContextSelectedDelImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new OnContextSelectedDel(this);
    }
}