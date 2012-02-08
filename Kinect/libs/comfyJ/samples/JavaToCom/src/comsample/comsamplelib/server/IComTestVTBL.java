/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
package comsample.comsamplelib.server;

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
import comsample.comsamplelib.*;
import comsample.comsamplelib.impl.*;

/**
 * Represents VTBL for COM interface IComTest
 */
public class IComTestVTBL extends IDispatchVTBL
{
    public IComTestVTBL(CoClassMetaInfo classMetaInfo)
    {
        super(classMetaInfo);

        addMembers(
            new VirtualMethodCallback[] {
                new VirtualMethodCallback(
                    "getString",
                    new HResult(),
                    new Parameter[] {
                        new Pointer.OutOnly(new BStr())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getInteger",
                    new HResult(),
                    new Parameter[] {
                        new Pointer.OutOnly(new Int())
                    },
                    0
                ),
                new VirtualMethodCallback(
                    "getVariant",
                    new HResult(),
                    new Parameter[] {
                        new Pointer.OutOnly(new Variant())
                    },
                    0
                )
            }
        );
    }
}