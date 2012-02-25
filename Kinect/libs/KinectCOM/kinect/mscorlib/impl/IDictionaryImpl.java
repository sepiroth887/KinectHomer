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
 * Represents COM interface IDictionary.
 *
 *
 */
public class IDictionaryImpl extends IDispatchImpl
    implements IDictionary
{
    public static final String INTERFACE_IDENTIFIER = IDictionary.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IDictionaryImpl()
    {
    }

    protected IDictionaryImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IDictionaryImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IDictionaryImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IDictionaryImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public VariantBool contains(
        Variant /*[in]*/ key)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                key,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void add(
        Variant /*[in]*/ key,
        Variant /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                key,
                value
            }
        );
    }

    /**
     * 
     */
    public void clear()
        throws ComException
    {
       invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    /**
     * 
     */
    public IDictionaryEnumerator getEnumerator()
        throws ComException
    {
        IDictionaryEnumeratorImpl pRetVal = new IDictionaryEnumeratorImpl();
       invokeStandardVirtualMethod(
            16,
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
    public void remove(
        Variant /*[in]*/ key)
        throws ComException
    {
       invokeStandardVirtualMethod(
            17,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                key
            }
        );
    }

    /**
     * 
     */
    public Variant getItem(
        Variant /*[in]*/ key)
        throws ComException
    {
        Variant pRetVal = new Variant();
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                key,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void setItem(
        Variant /*[in]*/ key,
        Variant /*[in]*/ pRetVal)
        throws ComException
    {
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                key,
                pRetVal
            }
        );
    }

    /**
     * 
     */
    public ICollection getKeys()
        throws ComException
    {
        ICollectionImpl pRetVal = new ICollectionImpl();
       invokeStandardVirtualMethod(
            9,
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
    public ICollection getValues()
        throws ComException
    {
        ICollectionImpl pRetVal = new ICollectionImpl();
       invokeStandardVirtualMethod(
            10,
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
    public VariantBool getIsReadOnly()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            14,
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
    public VariantBool getIsFixedSize()
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            15,
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
        return new IDictionaryImpl(this);
    }
}
