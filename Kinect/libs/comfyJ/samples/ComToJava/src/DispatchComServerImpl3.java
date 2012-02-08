/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */

import com.jniwrapper.win32.com.DispatchComServer;
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.automation.types.BStr;

/**
 * @author Vladimir Kondrashchenko
 */
public class DispatchComServerImpl3 extends DispatchComServer implements ICustom3
{
    public static final CLSID COM_SERVER_CLSID = new CLSID("{9D053F45-27FE-47e6-AEF5-C83F6C828379}");

    public DispatchComServerImpl3(CoClassMetaInfo classImpl)
    {
        super(classImpl);
    }

    public BStr getBstr()
    {
        return new BStr("Call to the ComServerImpl3#ICustom3 inteface.");
    }
}