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
 * Represents VTBL for COM interface IContainer.
 */
public class IContainerVTBL extends IDispatchVTBL
{
    public IContainerVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "add",
                    new HResult(),
                    new Parameter[] {
                        new IComponentImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "add_2",
                    new HResult(),
                    new Parameter[] {
                        new IComponentImpl(),
                        new BStr()
                    }
                ),
                new VirtualMethodCallback(
                    "getComponents",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new _ComponentCollectionImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "remove",
                    new HResult(),
                    new Parameter[] {
                        new IComponentImpl()
                    }
                )
            }
        );
    }
}