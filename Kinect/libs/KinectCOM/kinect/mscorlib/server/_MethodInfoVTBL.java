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
 * Represents VTBL for COM interface _MethodInfo.
 */
public class _MethodInfoVTBL extends IUnknownVTBL
{
    public _MethodInfoVTBL(CoClassMetaInfo classMetaInfo)
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
                    "getParameters",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getMethodImplementationFlags",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MethodImplAttributes())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getMethodHandle",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(RuntimeMethodHandle.class)
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAttributes",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new MethodAttributes())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getCallingConvention",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new CallingConventions())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "invoke_2",
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
                    "getIsPublic",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsPrivate",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsFamily",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsAssembly",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsFamilyAndAssembly",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsFamilyOrAssembly",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsStatic",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsFinal",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsVirtual",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsHideBySig",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsAbstract",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
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
                    "getIsConstructor",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "invoke_3",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new SafeArray(),
                        new Pointer(new Variant())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getReturnType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getReturnTypeCustomAttributes",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new ICustomAttributeProviderImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getBaseDefinition",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                )
            }
        );
    }
}