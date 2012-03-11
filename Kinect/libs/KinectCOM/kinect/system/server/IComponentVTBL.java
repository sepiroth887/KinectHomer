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
 * Represents VTBL for COM interface IComponent.
 */
public class IComponentVTBL extends IDispatchVTBL
{
    public IComponentVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getSite",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new ISiteImpl())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "setSite",
                    new HResult(),
                    new Parameter[] {
                        new ISiteImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "add_Disposed",
                    new HResult(),
                    new Parameter[] {
                        new _EventHandlerImpl()
                    }
                ),
                new VirtualMethodCallback(
                    "remove_Disposed",
                    new HResult(),
                    new Parameter[] {
                        new _EventHandlerImpl()
                    }
                )
            }
        );
    }
}