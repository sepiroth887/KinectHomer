/*
 * Copyright (c) 2000-2009 TeamDev Ltd. All rights reserved.
 * TeamDev PROPRIETARY and CONFIDENTIAL.
 * Use is subject to license terms.
 */
import com.jniwrapper.win32.com.server.CoClassMetaInfo;
import com.jniwrapper.win32.com.types.CLSID;
import com.jniwrapper.win32.com.DispatchComServer;
import com.jniwrapper.win32.com.IPersist;
import com.jniwrapper.win32.com.ComException;

/**
 * @author Vladimir Kondrashchenko
 */
public class DispatchComServerImpl extends DispatchComServer implements IPersist
{
    public static final CLSID COM_SERVER_CLSID = new CLSID("{69D4BFB2-2448-4175-997A-E94A4F797FA7}");
    public static final String PROG_ID = "jniwrapper.comtojava.1";
    public static final String VERSION_INDEPENDENT_PROG_ID = "jniwrapper.comtojava";
    public static final String COM_SERVER_NAME = "DispatchComServerImpl - my first Java COM Server.";

    public DispatchComServerImpl(CoClassMetaInfo classImpl)
    {
        super(classImpl);
    }

    public void getClassID(CLSID pClassID) throws ComException
    {
        pClassID.setData1(445);
        pClassID.setData2(10);
        pClassID.setData3(1023);
    }
}