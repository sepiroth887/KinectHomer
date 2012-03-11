package kinect.kinectcom;

import com.jniwrapper.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.ole.*;
import kinect.kinectcom.impl.*;
import kinect.mscorlib.impl.*;

/**
 * Represents COM coclass Attributes.
 */
public class Attributes extends CoClass
{
    public static final CLSID CLASS_ID = CLSID.create("{8EE0015C-7305-370A-B61D-708EDB7655F5}");

    public Attributes()
    {
    }

    public Attributes(Attributes that)
    {
        super(that);
    }

    /**
     * Creates coclass and returns its default interface.
     */
    public static _Attributes create(ClsCtx dwClsContext) throws ComException
    {
        final _AttributesImpl intf = new _AttributesImpl(CLASS_ID, dwClsContext);
        OleFunctions.oleRun(intf);
        return intf;
    }

    /**
     * Queries the <code>_Attributes</code> interface from IUnknown instance.
     */
    public static _Attributes queryInterface(IUnknown unknown) throws ComException
    {
        final _AttributesImpl result = new _AttributesImpl();
        unknown.queryInterface(result.getIID(), result);
        return result;
    }

    public CLSID getCLSID()
    {
        return CLASS_ID;
    }

    public Object clone()
    {
        return new Attributes(this);
    }
}