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
 * Represents COM interface IContainer.
 *
 *
 */
public class IContainerImpl extends IDispatchImpl
    implements IContainer
{
    public static final String INTERFACE_IDENTIFIER = IContainer.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IContainerImpl()
    {
    }

    protected IContainerImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IContainerImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IContainerImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IContainerImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void add(
        IComponent /*[in]*/ component)
        throws ComException
    {
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                component == null ? (Parameter)PTR_NULL : (Parameter)component
            }
        );
    }

    /**
     * 
     */
    public void add_2(
        IComponent /*[in]*/ component,
        BStr /*[in]*/ name)
        throws ComException
    {
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                component == null ? (Parameter)PTR_NULL : (Parameter)component,
                name == null ? (Parameter)PTR_NULL : name
            }
        );
    }

    /**
     * 
     */
    public void remove(
        IComponent /*[in]*/ component)
        throws ComException
    {
       invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                component == null ? (Parameter)PTR_NULL : (Parameter)component
            }
        );
    }

    /**
     * 
     */
    public _ComponentCollection getComponents()
        throws ComException
    {
        _ComponentCollectionImpl pRetVal = new _ComponentCollectionImpl();
       invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer((Parameter)pRetVal)
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
        return new IContainerImpl(this);
    }
}
