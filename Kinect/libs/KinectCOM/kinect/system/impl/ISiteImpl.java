package kinect.system.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.*;
import kinect.mscorlib.impl.*;
import kinect.system.*;

/**
 * Represents COM interface ISite.
 *
 *
 */
public class ISiteImpl extends IDispatchImpl
    implements ISite
{
    public static final String INTERFACE_IDENTIFIER = ISite.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public ISiteImpl()
    {
    }

    protected ISiteImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public ISiteImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public ISiteImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public ISiteImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public IComponent getComponent()
        throws ComException
    {
        IComponentImpl pRetVal = new IComponentImpl();
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public IContainer getContainer()
        throws ComException
    {
        IContainerImpl pRetVal = new IContainerImpl();
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public VariantBool getDesignMode()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public BStr getName()
        throws ComException
    {
        BStr pRetVal = new BStr();
       invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void setName(
        BStr /*[in]*/ pRetVal)
        throws ComException
    {
       invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : pRetVal
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new ISiteImpl(this);
    }
}
