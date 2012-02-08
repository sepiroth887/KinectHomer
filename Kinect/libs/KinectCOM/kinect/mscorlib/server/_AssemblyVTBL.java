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
 * Represents VTBL for COM interface _Assembly.
 */
public class _AssemblyVTBL extends IDispatchVTBL
{
    public _AssemblyVTBL(CoClassMetaInfo classMetaInfo)
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
                    "getCodeBase",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getEscapedCodeBase",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getName",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _AssemblyNameImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getName_2",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new _AssemblyNameImpl())
                    },
                    1
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
                    "getEntryPoint",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _MethodInfoImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getType_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _TypeImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getType_3",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new VariantBool(),
                        new Pointer(new _TypeImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getExportedTypes",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getTypes",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getManifestResourceStream",
                    new HResult(),
                    new Parameter[] {
                        new _TypeImpl(),
                        new BStr(),
                        new Pointer(new _StreamImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "getManifestResourceStream_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _StreamImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getFile",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _FileStreamImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getFiles",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getFiles_2",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getManifestResourceNames",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getManifestResourceInfo",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _ManifestResourceInfoImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getLocation",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getEvidence",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _EvidenceImpl())
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
                    "getObjectData",
                    new HResult(),
                    new Parameter[] {
                        new _SerializationInfoImpl(),
                        new StreamingContext()
                    }
                ),
                new VirtualMethodCallback(
                    "add_ModuleResolve",
                    new HResult(),
                    new Parameter[] {
                        new _ModuleResolveEventHandlerImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "remove_ModuleResolve",
                    new HResult(),
                    new Parameter[] {
                        new _ModuleResolveEventHandlerImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "getType_4",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new VariantBool(),
                        new VariantBool(),
                        new Pointer(new _TypeImpl())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "getSatelliteAssembly",
                    new HResult(),
                    new Parameter[] {
                        new _CultureInfoImpl(),
                        new Pointer(new _AssemblyImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getSatelliteAssembly_2",
                    new HResult(),
                    new Parameter[] {
                        new _CultureInfoImpl(),
                        new _VersionImpl(),
                        new Pointer(new _AssemblyImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "loadModule",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new SafeArray(),
                        new Pointer(new _ModuleImpl())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "loadModule_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new SafeArray(),
                        new SafeArray(),
                        new Pointer(new _ModuleImpl())
                    },
                    3
                ),
                new VirtualMethodCallback(
                    "createInstance",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new Variant())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "createInstance_2",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new VariantBool(),
                        new Pointer(new Variant())
                    },
                    2
                ),
                new VirtualMethodCallback(
                    "createInstance_3",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new VariantBool(),
                        new BindingFlags(),
                        new _BinderImpl(),
                        new SafeArray(),
                        new _CultureInfoImpl(),
                        new SafeArray(),
                        new Pointer(new Variant())
                    },
                    7
                ),
                new VirtualMethodCallback(
                    "getLoadedModules",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getLoadedModules_2",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getModules",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getModules_2",
                    new HResult(),
                    new Parameter[] {
                        new VariantBool(),
                        new Pointer(new SafeArray())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getModule",
                    new HResult(),
                    new Parameter[] {
                        new BStr(),
                        new Pointer(new _ModuleImpl())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "getReferencedAssemblies",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new SafeArray())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getGlobalAssemblyCache",
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