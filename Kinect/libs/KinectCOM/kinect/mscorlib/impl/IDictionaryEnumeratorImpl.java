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
 * Represents COM interface IDictionaryEnumerator.
 *
 *
 */
public class IDictionaryEnumeratorImpl extends IDispatchImpl
    implements IDictionaryEnumerator
{
    public static final String INTERFACE_IDENTIFIER = IDictionaryEnumerator.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IDictionaryEnumeratorImpl()
    {
    }

    protected IDictionaryEnumeratorImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IDictionaryEnumeratorImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IDictionaryEnumeratorImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IDictionaryEnumeratorImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public Variant getKey()
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            7,
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
    public Variant getValue()
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            8,
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
    public DictionaryEntry getEntry()
        throws ComException
    {
        DictionaryEntry pRetVal = new DictionaryEntry();
       invokeStandardVirtualMethod(
            9,
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
        return new IDictionaryEnumeratorImpl(this);
    }
}
