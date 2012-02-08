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
 * Represents VTBL for COM interface ICustomAttributeProvider.
 */
public class ICustomAttributeProviderVTBL extends IDispatchVTBL
{
    public ICustomAttributeProviderVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
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
                )
            }
        );
    }
}