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
 * Represents COM interface IComponent.
 *
 *
 */
public class IComponentImpl extends IDispatchImpl
    implements IComponent
{
    public static final String INTERFACE_IDENTIFIER = IComponent.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IComponentImpl()
    {
    }

    protected IComponentImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IComponentImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IComponentImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IComponentImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void add_Disposed(
        _EventHandler /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                value == null ? (Parameter)PTR_NULL : (Parameter)value
            }
        );
    }

    /**
     * 
     */
    public void remove_Disposed(
        _EventHandler /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                value == null ? (Parameter)PTR_NULL : (Parameter)value
            }
        );
    }

    /**
     * 
     */
    public ISite getSite()
        throws ComException
    {
        ISiteImpl pRetVal = new ISiteImpl();
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
    public void setSite(
        ISite /*[in]*/ pRetVal)
        throws ComException
    {
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : (Parameter)pRetVal
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IComponentImpl(this);
    }
}
