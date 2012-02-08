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
 * Represents VTBL for COM interface _Binder.
 */
public class _BinderVTBL extends IDispatchVTBL
{
    public _BinderVTBL(CoClassMetaInfo classMetaInfo)
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
                    "bindToMethod",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new SafeArray(),
                        new Pointer(new SafeArray()),
                        new SafeArray(),
                        new _CultureInfoImpl(),
                        new SafeArray(),
                        new Pointer(new Variant()),
                        new Pointer(new _MethodBaseImpl())
                    },
                    7
                ),
                new VirtualMethodCallback(
                    "bindToField",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new SafeArray(),
                        new Variant(),
                        new _CultureInfoImpl(),
                        new Pointer(new _FieldInfoImpl())
                    },
                    4
                ),
                new VirtualMethodCallback(
                    "selectMethod",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new SafeArray(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _MethodBaseImpl())
                    },
                    4
                ),
                new VirtualMethodCallback(
                    "selectProperty",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new SafeArray(),
                        new _TypeImpl(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    5
                ),
                new VirtualMethodCallback(
                    "changeType",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new _TypeImpl(),
                        new _CultureInfoImpl(),
                        new Pointer(new Variant())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "reorderArgumentArray",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray()),
                        new Variant()
                    }
                )
            }
        );
    }
}