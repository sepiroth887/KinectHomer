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
 * Represents VTBL for COM interface _Type.
 */
public class _TypeVTBL extends IUnknownVTBL
{
    public _TypeVTBL(CoClassMetaInfo classMetaInfo)
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
                    "getGuid",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(GUID.class)
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getModule",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _ModuleImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAssembly",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _AssemblyImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getTypeHandle",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(RuntimeTypeHandle.class)
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getFullName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getNamespace",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getAssemblyQualifiedName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getArrayRank",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getBaseType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getConstructors",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getInterface",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new VariantBool(),
                        new Pointer(new _TypeImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getInterfaces",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "findInterfaces",
                    new HResult(),
                    new Parameter[] {
                        new _TypeFilterImpl(),
                        new Variant(),
                        new Pointer(new SafeArray())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getEvent",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new Pointer(new _EventInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getEvents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getEvents_2",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getNestedTypes",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getNestedType",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new Pointer(new _TypeImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getMember",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new MemberTypes(),
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "getDefaultMembers",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "findMembers",
                    new HResult(),
                    new Parameter[] {
                        new MemberTypes(),
                        new BindingFlags(),
                        new _MemberFilterImpl(),
                        new Variant(),
                        new Pointer(new SafeArray())
                    },
                    4
                ),
                new VirtualMethodCallback(
                    "getElementType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "isSubclassOf",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "isInstanceOfType",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "isAssignableFrom",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getInterfaceMap",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new Pointer(InterfaceMapping.class)
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMethod",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    5
                ),
                new VirtualMethodCallback(
                    "getMethod_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getMethods",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getField",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new Pointer(new _FieldInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getFields",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getProperty",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getProperty_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new _TypeImpl(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    6
                ),
                new VirtualMethodCallback(
                    "getProperties",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMember_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getMembers",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "invokeMember",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new Variant(),
                        new SafeArray(),
                        new SafeArray(),
                        new _CultureInfoImpl(),
                        new SafeArray(),
                        new Pointer(new Variant())
                    },
                    8
                ),
                new VirtualMethodCallback(
                    "getUnderlyingSystemType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _TypeImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "invokeMember_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new Variant(),
                        new SafeArray(),
                        new _CultureInfoImpl(),
                        new Pointer(new Variant())
                    },
                    6
                ),
                new VirtualMethodCallback(
                    "invokeMember_3",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new Variant(),
                        new SafeArray(),
                        new Pointer(new Variant())
                    },
                    5
                ),
                new VirtualMethodCallback(
                    "getConstructor",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new _BinderImpl(),
                        new CallingConventions(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _ConstructorInfoImpl())
                    },
                    5
                ),
                new VirtualMethodCallback(
                    "getConstructor_2",
                    new HResult(),
                    new Parameter[] {
                        new BindingFlags(),
                        new _BinderImpl(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _ConstructorInfoImpl())
                    },
                    4
                ),
                new VirtualMethodCallback(
                    "getConstructor_3",
                    new HResult(),
                    new Parameter[] {
                        new SafeArray(),
                        new Pointer(new _ConstructorInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getConstructors_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getTypeInitializer",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _ConstructorInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getMethod_3",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new CallingConventions(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    6
                ),
                new VirtualMethodCallback(
                    "getMethod_4",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "getMethod_5",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new SafeArray(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getMethod_6",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _MethodInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMethods_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getField_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _FieldInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getFields_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getInterface_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _TypeImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getEvent_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _EventInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getProperty_3",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new _TypeImpl(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    4
                ),
                new VirtualMethodCallback(
                    "getProperty_4",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new _TypeImpl(),
                        new SafeArray(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "getProperty_5",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new SafeArray(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getProperty_6",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new _TypeImpl(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getProperty_7",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _PropertyInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getProperties_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getNestedTypes_2",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getNestedType_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _TypeImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMember_3",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getMembers_2",
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
                        new Pointer(new TypeAttributes())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsNotPublic",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
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
                    "getIsNestedPublic",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsNestedPrivate",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsNestedFamily",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsNestedAssembly",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsNestedFamANDAssem",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsNestedFamORAssem",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsAutoLayout",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsLayoutSequential",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsExplicitLayout",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsClass",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsInterface",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsValueType",
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
                    "getIsSealed",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsEnum",
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
                    "getIsImport",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsSerializable",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsAnsiClass",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsUnicodeClass",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsAutoClass",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsArray",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsByRef",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsPointer",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsPrimitive",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsCOMObject",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getHasElementType",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsContextful",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsMarshalByRef",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "equals_2",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new Pointer(new VariantBool())
                    },
                    1
                )
            }
        );
    }
}