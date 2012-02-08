package kinect.mscorlib.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.*;
import com.jniwrapper.win32.automation.impl.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import com.jniwrapper.win32.com.types.GUID;
import kinect.mscorlib.*;
import kinect.mscorlib.impl.*;

/**
 * Represents VTBL for COM interface _EventInfo.
 */
public class _EventInfoVTBL extends IUnknownVTBL
{
    public _EventInfoVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getTypeInfoCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new UInt32())
                    }
                ),
                new VirtualMethodCallback(
                    "getTypeInfo",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new UInt32(),
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getIDsOfNames",
                    new HResult(),
                    new Parameter[] {
                        new Pointer.Const(GUID.class),
                        new Int32(),
                        new UInt32(),
                        new UInt32(),
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "invoke",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new Pointer.Const(GUID.class),
                        new UInt32(),
                        new Int16(),
                        new Int32(),
                        new Int32(),
                        new Int32(),
                        new Int32()
                    }
                ),
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
                    "getMemberType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MemberTypes())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getDeclaringType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getReflectedType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCustomAttributes",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new VariantBool(),
                        new Pointer(new SafeArray())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getCustomAttributes_2",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "isDefined",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new VariantBool(),
                        new Pointer(new VariantBool())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getAddMethod",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getRemoveMethod",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getRaiseMethod",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getAttributes",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new EventAttributes())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAddMethod_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getRemoveMethod_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getRaiseMethod_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "addEventHandler",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new _DelegateImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "removeEventHandler",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new _DelegateImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "getEventHandlerType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsSpecialName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsMulticast",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                )
            }
        );
    }
}