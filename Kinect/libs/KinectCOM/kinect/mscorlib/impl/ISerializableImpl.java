package kinect.mscorlib.impl;

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

/**
 * Represents COM interface ISerializable.
 *
 *
 */
public class ISerializableImpl extends IDispatchImpl
    implements ISerializable
{
    public static final String INTERFACE_IDENTIFIER = ISerializable.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public ISerializableImpl()
    {
    }

    protected ISerializableImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public ISerializableImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public ISerializableImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public ISerializableImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void getObjectData(
        _SerializationInfo /*[in]*/ info,
        StreamingContext /*[in]*/ Context)
        throws ComException
    {
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                info == null ? (Parameter)PTR_NULL : (Parameter)info,
                Context
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new ISerializableImpl(this);
    }
}
