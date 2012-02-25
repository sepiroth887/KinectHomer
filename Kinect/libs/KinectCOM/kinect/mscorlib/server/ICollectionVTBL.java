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
 * Represents VTBL for COM interface ICollection.
 */
public class ICollectionVTBL extends IDispatchVTBL
{
    public ICollectionVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "copyTo",
                    new HResult(),
                    new Parameter[] {
                        new _ArrayImpl(),
                        new Int32()
                    }
                ),
                new VirtualMethodCallback(
                    "getCount",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getSyncRoot",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getIsSynchronized",
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