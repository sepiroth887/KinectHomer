package kinect.stdole.server;

import com.jniwrapper.*;
import com.jniwrapper.win32.*;
import com.jniwrapper.win32.automation.types.*;
import com.jniwrapper.win32.com.*;
import com.jniwrapper.win32.com.impl.*;
import com.jniwrapper.win32.com.server.*;
import com.jniwrapper.win32.com.types.*;
import kinect.stdole.*;
import kinect.stdole.impl.*;

/**
 * Represents VTBL for COM interface IEnumVARIANT.
 */
public class IEnumVARIANTVTBL extends IUnknownVTBL
{
    public IEnumVARIANTVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "next",
                    new HResult(),
                    new Parameter[] {
                        new UInt32(),
                        new Pointer.Const(new Variant()),
                        new Pointer(new UInt32())
                    }
                ),
                new VirtualMethodCallback(
                    "skip",
                    new HResult(),
                    new Parameter[] {
                        new UInt32()
                    }
                ),
                new VirtualMethodCallback(
                    "reset",
                    new HResult(),
                    new Parameter[] {
                    }
                ),
                new VirtualMethodCallback(
                    "clone_",
                    new HResult(),
                    new Parameter[] {
                        new Pointer(new IEnumVARIANTImpl())
                    }
                )
            }
        );
    }
}