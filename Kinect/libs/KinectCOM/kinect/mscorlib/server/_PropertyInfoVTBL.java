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
 * Represents VTBL for COM interface _PropertyInfo.
 */
public class _PropertyInfoVTBL extends IUnknownVTBL
{
    public _PropertyInfoVTBL(CoClassMetaInfo classMetaInfo)
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
                    "getPropertyType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getValue",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new SafeArray(),
                        new Pointer(new Variant())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getValue_2",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new SafeArray(),
                        new _CultureInfoImpl(),
                        new Pointer(new Variant())
                    },
                    5
                ),
                new VirtualMethodCallback(
                    "setValue",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Variant(),
                        new SafeArray()
                    }
                ),
                new VirtualMethodCallback(
                    "setValue_2",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Variant(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new SafeArray(),
                        new _CultureInfoImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "getAccessors",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getGetMethod",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getSetMethod",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getIndexParameters",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAttributes",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new PropertyAttributes())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCanRead",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCanWrite",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAccessors_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getGetMethod_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSetMethod_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
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
                )
            }
        );
    }
}