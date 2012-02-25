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
 * Represents VTBL for COM interface IDictionaryEnumerator.
 */
public class IDictionaryEnumeratorVTBL extends IDispatchVTBL
{
    public IDictionaryEnumeratorVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getKey",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getValue",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Variant())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getEntry",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(DictionaryEntry.class)
                    },
                    0
                )
            }
        );
    }
}