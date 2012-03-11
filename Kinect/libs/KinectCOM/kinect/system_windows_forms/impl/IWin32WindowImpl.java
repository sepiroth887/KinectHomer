package kinect.system_windows_forms.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import kinect.system_windows_forms.*;

/**
 * Represents COM interface IWin32Window.
 *
 *
 */
public class IWin32WindowImpl extends IUnknownImpl
    implements IWin32Window
{
    public static final String INTERFACE_IDENTIFIER = IWin32Window.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IWin32WindowImpl()
    {
    }

    protected IWin32WindowImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IWin32WindowImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IWin32WindowImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IWin32WindowImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public Int32 getHandle()
        throws ComException
    {
        Int32 pRetVal = new Int32();
       invokeStandardVirtualMethod(
            3,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IWin32WindowImpl(this);
    }
}
