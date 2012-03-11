package kinect.system_windows_forms.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import kinect.system_windows_forms.*;
import kinect.system_windows_forms.impl.*;

/**
 * Represents VTBL for COM interface IWin32Window.
 */
public class IWin32WindowVTBL extends IUnknownVTBL
{
    public IWin32WindowVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getHandle",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new Int32())
                    },
                    0
                )
            }
        );
    }
}