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
public class DispatchComServerImpl2 extends DispatchComServer implements ICustom2
{
    public static final CLSID COM_SERVER_CLSID = new CLSID("{B941491E-227D-46a6-91DF-20B45942CBE1}");

    public DispatchComServerImpl2(CoClassMetaInfo classImpl)
    {
        super(classImpl);
    }

    public BStr getBStr()
    {
        return new BStr("Call to the ComServerImpl2#ICustom2 interface.");
    }
}