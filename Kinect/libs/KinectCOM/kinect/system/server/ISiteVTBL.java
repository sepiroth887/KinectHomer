package kinect.system.server;

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
import kinect.system.*;
import kinect.system.impl.*;

/**
 * Represents VTBL for COM interface ISite.
 */
public class ISiteVTBL extends IDispatchVTBL
{
    public ISiteVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getComponent",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IComponentImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getContainer",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IContainerImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getDesignMode",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new VariantBool())
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
                    "setName",
                    new HResult(),
                    new Parameter[] {
                        new BStr()
                    }
                )
            }
        );
    }
}