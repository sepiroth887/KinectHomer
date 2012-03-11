package kinect.kinectcom.impl;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.kinectcom.*;
import kinect.mscorlib.*;
import kinect.mscorlib.impl.*;
import kinect.stdole.*;
import kinect.stdole.impl.*;

/**
 * Represents COM interface IKinect.
 *
 *
 */
public class IKinectImpl extends IDispatchImpl
    implements IKinect
{
    public static final String INTERFACE_IDENTIFIER = IKinect.INTERFACE_IDENTIFIER;
    private static final IID _iid = IID.create(INTERFACE_IDENTIFIER);

    public IKinectImpl()
    {
    }

    protected IKinectImpl(IUnknownImpl that) throws ComException
    {
        super(that);
    }

    public IKinectImpl(IUnknown that) throws ComException
    {
        super(that);
    }

    public IKinectImpl(CLSID clsid, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, dwClsContext);
    }

    public IKinectImpl(CLSID clsid, IUnknownImpl pUnkOuter, ClsCtx dwClsContext) throws ComException
    {
        super(clsid, pUnkOuter, dwClsContext);
    }

    /**
     * 
     */
    public void init()
        throws ComException
    {
       invokeStandardVirtualMethod(
            7,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    /**
     * 
     */
    public void uninit()
        throws ComException
    {
       invokeStandardVirtualMethod(
            8,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    /**
     * 
     */
    public void learnUser(
        BStr /*[in]*/ name,
        Int32 /*[in]*/ skeletonID)
        throws ComException
    {
       invokeStandardVirtualMethod(
            9,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                name == null ? (Parameter)PTR_NULL : name,
                skeletonID
            }
        );
    }

    /**
     * 
     */
    public VariantBool startTracking(
        Int32 /*[in]*/ skeletonID)
        throws ComException
    {
        VariantBool pRetVal = new VariantBool();
       invokeStandardVirtualMethod(
            10,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                skeletonID,
                pRetVal == null ? (Parameter)PTR_NULL : new Pointer(pRetVal)
            }
        );
        return pRetVal;
    }

    /**
     * 
     */
    public void stopTracking(
        Int32 /*[in]*/ skeletonID)
        throws ComException
    {
       invokeStandardVirtualMethod(
            11,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                skeletonID
            }
        );
    }

    /**
     * 
     */
    public void gestureRecordCompleted(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
        throws ComException
    {
       invokeStandardVirtualMethod(
            12,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                gestureName == null ? (Parameter)PTR_NULL : gestureName,
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            }
        );
    }

    /**
     * 
     */
    public void recordingCountdownEvent(
        Int32 /*[in]*/ p)
        throws ComException
    {
       invokeStandardVirtualMethod(
            13,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                p
            }
        );
    }

    /**
     * 
     */
    public void gestureRecognitionCompleted(
        BStr /*[in]*/ Gesture)
        throws ComException
    {
       invokeStandardVirtualMethod(
            14,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                Gesture == null ? (Parameter)PTR_NULL : Gesture
            }
        );
    }

    /**
     * 
     */
    public void recordGesture(
        BStr /*[in]*/ gestureName,
        BStr /*[in]*/ ctxt)
        throws ComException
    {
       invokeStandardVirtualMethod(
            15,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                gestureName == null ? (Parameter)PTR_NULL : gestureName,
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            }
        );
    }

    /**
     * 
     */
    public void recognizeGesture(
        BStr /*[in]*/ ctxt)
        throws ComException
    {
       invokeStandardVirtualMethod(
            16,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            }
        );
    }

    /**
     * 
     */
    public void stopRecGesture()
        throws ComException
    {
       invokeStandardVirtualMethod(
            17,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    /**
     * 
     */
    public void onAddOnGestureValueChange(
        SingleFloat /*[in]*/ value)
        throws ComException
    {
       invokeStandardVirtualMethod(
            18,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                value
            }
        );
    }

    /**
     * 
     */
    public void contextSelected(
        BStr /*[in]*/ ctxt)
        throws ComException
    {
       invokeStandardVirtualMethod(
            19,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                ctxt == null ? (Parameter)PTR_NULL : ctxt
            }
        );
    }

    /**
     * 
     */
    public void storeGestures()
        throws ComException
    {
       invokeStandardVirtualMethod(
            20,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[0]
        );
    }

    /**
     * 
     */
    public IEnumerable loadGestures()
        throws ComException
    {
        IEnumerableImpl pRetVal = new IEnumerableImpl();
       invokeStandardVirtualMethod(
            21,
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
    public void setTrackingStrategy(
        Int32 /*[in]*/ strat)
        throws ComException
    {
       invokeStandardVirtualMethod(
            22,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                strat
            }
        );
    }

    /**
     * 
     */
    public void trackingStarted(
        Int32 /*[in]*/ matchingSkeleton)
        throws ComException
    {
       invokeStandardVirtualMethod(
            23,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                matchingSkeleton
            }
        );
    }

    /**
     * 
     */
    public void trackingStopped(
        Int32 /*[in]*/ matchingSkeleton)
        throws ComException
    {
       invokeStandardVirtualMethod(
            24,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                matchingSkeleton
            }
        );
    }

    /**
     * 
     */
    public BStr getObjects()
        throws ComException
    {
        BStr pRetVal = new BStr();
       invokeStandardVirtualMethod(
            25,
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
    public void userDetected(
        _User /*[in]*/ User)
        throws ComException
    {
       invokeStandardVirtualMethod(
            26,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                User == null ? (Parameter)PTR_NULL : (Parameter)User
            }
        );
    }

    /**
     * 
     */
    public void userLost(
        _User /*[in]*/ User)
        throws ComException
    {
       invokeStandardVirtualMethod(
            27,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                User == null ? (Parameter)PTR_NULL : (Parameter)User
            }
        );
    }

    /**
     * 
     */
    public void setDefaultHand(
        VariantBool /*[in]*/ def)
        throws ComException
    {
       invokeStandardVirtualMethod(
            28,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                def
            }
        );
    }

    /**
     * 
     */
    public BStr loadUsers()
        throws ComException
    {
        BStr pRetVal = new BStr();
       invokeStandardVirtualMethod(
            29,
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
    public void addUser(
        BStr /*[in]*/ User)
        throws ComException
    {
       invokeStandardVirtualMethod(
            30,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                User == null ? (Parameter)PTR_NULL : User
            }
        );
    }

    /**
     * 
     */
    public void delUser(
        BStr /*[in]*/ User)
        throws ComException
    {
       invokeStandardVirtualMethod(
            31,
            Function.STDCALL_CALLING_CONVENTION,
            new Parameter[] {
                User == null ? (Parameter)PTR_NULL : User
            }
        );
    }

    public IID getIID()
    {
        return _iid;
    }

    public Object clone()
    {
        return new IKinectImpl(this);
    }
}
