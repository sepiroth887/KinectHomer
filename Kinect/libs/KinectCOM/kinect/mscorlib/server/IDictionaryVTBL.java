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
 * Represents VTBL for COM interface IDictionary.
 */
public class IDictionaryVTBL extends IDispatchVTBL
{
    public IDictionaryVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getItem",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Pointer(new Variant())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "setItem",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Variant()
                    }
                ),
                new VirtualMethodCallback(
                    "getKeys",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new ICollectionImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getValues",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new ICollectionImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "contains",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Pointer(new VariantBool())
                    },
                    1
                ),
                new VirtualMethodCallback(
                    "add",
                    new HResult(),
                    new Parameter[] {
                        new Variant(),
                        new Variant()
                    }
                ),
                new VirtualMethodCallback(
                    "clear",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "getIsReadOnly",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsFixedSize",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getEnumerator",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IDictionaryEnumeratorImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "remove",
                    new HResult(),
                    new Parameter[] {
                        new Variant()
                    }
                )
            }
        );
    }
}