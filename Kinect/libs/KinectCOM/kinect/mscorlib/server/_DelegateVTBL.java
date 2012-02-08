package kinect.mscorlib.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.server.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.*;
import kinect.mscorlib.impl.*;

/**
 * Represents VTBL for COM interface _Delegate.
 */
public class _DelegateVTBL extends IDispatchVTBL
{
    public _DelegateVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getToString",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "equals_",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getHashCode",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getInvocationList",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "clone_",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getObjectData",
                    new HResult(),
                    new Parameter[] {
                        new _SerializationInfoImpl(),
                        new StreamingContext()
                    }
                ),
                new VirtualMethodCallback(
                    "dynamicInvoke",
                    new HResult(),
                    new Parameter[] {
                        new SafeArray(),
                        new Pointer(new Variant())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMethod",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getTarget",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                )
            }
        );
    }
}